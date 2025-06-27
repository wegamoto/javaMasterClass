import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'login_page.dart';
import 'orders_monitor_page.dart'; // เพิ่มตรงนี้

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  Future<void> _logout(BuildContext context) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('jwt_token');

    Navigator.pushAndRemoveUntil(
      context,
      MaterialPageRoute(builder: (context) => const LoginPage()),
          (route) => false,
    );
  }

  void _onMenuSelected(BuildContext context, String value) {
    switch (value) {
      case 'dashboard':
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('📊 Dashboard Clicked')),
        );
        break;
      case 'orders':
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const OrdersMonitorPage()),
        );
        break;
      case 'profile':
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('👤 Profile Clicked')),
        );
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('🏠 Home'),
        actions: [
          PopupMenuButton<String>(
            onSelected: (value) => _onMenuSelected(context, value),
            itemBuilder: (BuildContext context) => [
              const PopupMenuItem(value: 'dashboard', child: Text('Dashboard')),
              const PopupMenuItem(value: 'orders', child: Text('Orders')),
              const PopupMenuItem(value: 'profile', child: Text('Profile')),
            ],
          ),
          IconButton(
            icon: const Icon(Icons.logout),
            tooltip: 'Logout',
            onPressed: () => _logout(context),
          ),
        ],
      ),
      body: const Center(
        child: Text(
          '🎉 Welcome to the Home Page!',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
