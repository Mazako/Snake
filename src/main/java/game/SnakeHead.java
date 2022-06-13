package game;

public class SnakeHead extends Snake {

    public SnakeHead(int x, int y, Snake previousSnake, Direction direction) {
        super(x, y, previousSnake, direction);
    }

    public Snake getLastNode() {
        Snake node = this;
        while (node.hasPreviousSnake()) {
            node = node.getPreviousSnake();
        }
        return node;
    }
}
