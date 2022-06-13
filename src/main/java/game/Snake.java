package game;

public class Snake {

    private int X;
    private int Y;

    private Snake previousSnake;

    private Direction direction;


    public Snake(int x, int y, Snake previousSnake, Direction direction) {
        X = x;
        Y = y;
        this.previousSnake = previousSnake;
        this.direction = direction;
    }

    public Snake getPreviousSnake() {
        return previousSnake;
    }

    public void setPreviousSnake(Snake previousSnake) {
        this.previousSnake = previousSnake;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }


    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public boolean hasPreviousSnake() {
        return previousSnake != null;
    }


}
