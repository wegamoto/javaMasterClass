import 'package:flutter/material.dart';
import '../models/product.dart';

class ProductItem extends StatelessWidget {
  final Product product;

  const ProductItem({super.key, required this.product});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 3,
      margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
      child: ListTile(
        leading: Image.network(product.imageUrl, width: 60, fit: BoxFit.cover),
        title: Text(product.title),
        subtitle: Text('฿${product.price.toStringAsFixed(2)}'),
      ),
    );
  }
}
