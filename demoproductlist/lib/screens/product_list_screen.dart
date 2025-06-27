import 'package:flutter/material.dart';
import '../models/product.dart';
import '../widgets/product_item.dart';

class ProductListScreen extends StatefulWidget {
  const ProductListScreen({super.key});

  @override
  State<ProductListScreen> createState() => _ProductListScreenState();
}

class _ProductListScreenState extends State<ProductListScreen> {
  final List<Product> _allProducts = [
    Product(id: '1', title: 'เสื้อยืด', imageUrl: 'https://via.placeholder.com/150', price: 250),
    Product(id: '2', title: 'กางเกงยีนส์', imageUrl: 'https://via.placeholder.com/150', price: 499),
    Product(id: '3', title: 'รองเท้า', imageUrl: 'https://via.placeholder.com/150', price: 800),
  ];

  String _searchQuery = '';

  @override
  Widget build(BuildContext context) {
    final filteredProducts = _allProducts.where((product) {
      return product.title.toLowerCase().contains(_searchQuery.toLowerCase());
    }).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('สินค้า'),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              decoration: const InputDecoration(
                labelText: 'ค้นหาสินค้า',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.search),
              ),
              onChanged: (value) => setState(() => _searchQuery = value),
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: filteredProducts.length,
              itemBuilder: (ctx, index) => ProductItem(product: filteredProducts[index]),
            ),
          ),
        ],
      ),
    );
  }
}
