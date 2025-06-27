import 'package:flame/components.dart';
import 'package:flame/game.dart';
import '../components/tile_component.dart';

class PuzzleGame extends FlameGame {
  final int gridSize = 3;

  @override
  Future<void> onLoad() async {
    super.onLoad();
    const double tileSize = 100;
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        if (row == gridSize - 1 && col == gridSize - 1) continue; // ช่องว่าง
        add(TileComponent(
          position: Vector2(col * tileSize, row * tileSize),
          size: Vector2(tileSize, tileSize),
          number: row * gridSize + col + 1,
        ));
      }
    }
  }
}
