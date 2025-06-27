import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class OrdersMonitorPage extends StatefulWidget {
  const OrdersMonitorPage({super.key});

  @override
  State<OrdersMonitorPage> createState() => _OrdersMonitorPageState();
}

class _OrdersMonitorPageState extends State<OrdersMonitorPage> {
  List<dynamic> orders = [];
  bool isLoading = true;
  String? errorMessage;

  @override
  void initState() {
    super.initState();
    fetchOrders();
  }

  Future<void> fetchOrders() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final token = prefs.getString('jwt_token');

      if (token == null) {
        setState(() {
          errorMessage = '🚫 Token not found. Please login again.';
          isLoading = false;
        });
        return;
      }

      final url = Uri.parse('https://shop.temjaiengineering.com/api/orders-monitor');
      final response = await http.get(
        url,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> jsonResponse = json.decode(response.body);

        if (jsonResponse['orders'] is List) {
          setState(() {
            orders = jsonResponse['orders'];
            isLoading = false;
          });
        } else {
          setState(() {
            errorMessage = '⚠️ Unexpected data format (no orders list)';
            isLoading = false;
          });
        }
      } else {
        setState(() {
          errorMessage = '⚠️ Failed to load orders (${response.statusCode})';
          isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        errorMessage = '❌ Error: $e';
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('📦 Orders Monitor')),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : errorMessage != null
          ? Center(child: Text(errorMessage!))
          : ListView.builder(
        itemCount: orders.length,
        itemBuilder: (context, index) {
          final order = orders[index];
          return Card(
            margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            child: ListTile(
              title: Text('Order #${order['id'] ?? 'N/A'}'),
              subtitle: Text('Status: ${order['status'] ?? 'N/A'}\nTotal: ${order['totalFormatted'] ?? 'N/A'}\nCreated: ${order['createdAt'] ?? ''}'),
              trailing: const Icon(Icons.arrow_forward_ios, size: 16),
            ),
          );
        },
      ),
    );
  }
}
