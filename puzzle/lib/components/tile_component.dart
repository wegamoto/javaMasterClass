import 'package:flame/components.dart';
import 'package:flutter/material.dart';

class TileComponent extends PositionComponent {
  final int number;

  TileComponent({
    required super.position,
    required super.size,
    required this.number,
  });

  @override
  void render(Canvas canvas) {
    super.render(canvas);
    final paint = Paint()..color = Colors.blueAccent;
    canvas.drawRect(size.toRect(), paint);

    final textPainter = TextPainter(
      text: TextSpan(
        text: number.toString(),
        style: const TextStyle(fontSize: 30, color: Colors.white),
      ),
      textDirection: TextDirection.ltr,
    )..layout();

    textPainter.paint(
      canvas,
      Offset(size.x / 2 - textPainter.width / 2, size.y / 2 - textPainter.height / 2),
    );
  }
}
