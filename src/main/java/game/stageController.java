package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class stageController {

    private boolean gameOver = false;

    private boolean changed = false;

    private final Random random = new Random();
    @FXML
    private Timeline animation;

    @FXML
    private Canvas gameCanvas;

    private int tick = 0;

    public void initialize() {
        gameCanvas.setFocusTraversable(true);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        SnakeHead snake = new SnakeHead(5,5, null, Direction.RIGHT);
        addNode(snake);
        addNode(snake);
        addNode(snake);
        Apple apple = new Apple(10, 10);
        gameCanvas.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> changeDirection(snake, keyEvent, gc));

        animation = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                    step(snake,gc, apple);
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();


        gameCanvas.addEventFilter(Event.ANY, event -> {
            if (gameOver) {
                animation.stop();
                ((Node)(event.getSource())).getScene().getWindow().hide();
                try {
                    AnchorPane load = FXMLLoader.load(getClass().getResource("/lose.fxml"));
                    Scene scene = new Scene(load);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }

    private void step(SnakeHead snake, GraphicsContext gc, Apple apple) {
        if (!gameOver) {
            changed = false;
            move(snake);
            checkCollision(snake, apple);
            gc.clearRect(0, 0, 800, 800);
            initYlines(gc);
            initXlines(gc);
            drawSnakes(snake, gc);
            drawApple(apple, gc);
            tick++;

        }

    }

    private void drawApple(Apple apple, GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(apple.getX() * 30, apple.getY() * 30, 30, 30 );
        gc.setFill(Color.BLACK);
    }


    private void checkCollision(SnakeHead snake, Apple apple) {
        ArrayList<Snake> snakes = new ArrayList<>();
        Snake node = snake;
        if (apple.getX() == snake.getX() && apple.getY() == snake.getY()) {
            addNode(snake);
            apple.setY(random.nextInt(20));
            apple.setX(random.nextInt(20));
        }
        while (node.hasPreviousSnake()) {
            snakes.add(node.getPreviousSnake());
            node = node.getPreviousSnake();
        }
        for (Snake s : snakes) {
            if (s.getX() == snake.getX() && s.getY() == snake.getY()) {
                gameOver = true;
                break;
            }
            if (apple.getX() == s.getX() && apple.getY() == s.getY()) {
                addNode(snake);
                apple.setY(random.nextInt(20));
                apple.setX(random.nextInt(20));
            }
        }


    }


    private void addNode(SnakeHead snake) {
        Snake lastSnake = snake.getLastNode();
        Direction direction;
        int X = 0;
        int Y = 0;
        switch (lastSnake.getDirection()) {
            case RIGHT -> {
                X = lastSnake.getX() - 1;
                Y = lastSnake.getY();
            }
            case LEFT -> {
                X = lastSnake.getX() + 1;
                Y = lastSnake.getY();
            }
            case UP -> {
                X = lastSnake.getX();
                Y = lastSnake.getY() + 1;
            }
            case DOWN -> {
                X = lastSnake.getX();
                Y = lastSnake.getY() - 1;
            }

        }
        lastSnake.setPreviousSnake(new Snake(X,Y,null, lastSnake.getDirection()));
    }

    private void move(Snake snake) {
        int prevX = snake.getX();
        int prevY = snake.getY();
        Direction prevDirection = snake.getDirection();
            switch (snake.getDirection()) {
                case UP -> {
                    snake.setY(snake.getY() - 1);
                    if (snake.getY() == -1)
                        snake.setY(19);
                }
                case DOWN -> {
                    snake.setY(snake.getY() + 1);
                    if (snake.getY() == 20)
                        snake.setY(0);
                }
                case LEFT -> {
                    snake.setX(snake.getX() - 1);
                    if (snake.getX() == -1)
                        snake.setX(19);
                }
                case RIGHT -> {
                    snake.setX(snake.getX() + 1);
                    if (snake.getX() == 20)
                        snake.setX(0);
                }
            }
            
            if (snake.hasPreviousSnake())
                moveOtherSnakes(prevX, prevY,prevDirection, snake.getPreviousSnake());

    }

    private void moveOtherSnakes(int prevX, int prevY, Direction prevDirection, Snake previousSnake) {
        int pX = previousSnake.getX();
        int pY = previousSnake.getY();
        Direction pD = previousSnake.getDirection();
        previousSnake.setX(prevX);
        previousSnake.setY(prevY);
        previousSnake.setDirection(prevDirection);
        if (previousSnake.hasPreviousSnake()) {
            moveOtherSnakes(pX,pY, pD, previousSnake.getPreviousSnake());
        }
    }


    private void changeDirection(SnakeHead snake, KeyEvent keyEvent, GraphicsContext gc) {
        KeyCode code = keyEvent.getCode();
        if (!changed) {
            switch (code) {
                case W -> {
                    if (snake.getDirection() != Direction.DOWN)
                        snake.setDirection(Direction.UP);
                }
                case S -> {
                    if (snake.getDirection() != Direction.UP)
                        snake.setDirection(Direction.DOWN);
                }

                case A -> {
                    if (snake.getDirection() != Direction.RIGHT)
                        snake.setDirection(Direction.LEFT);
                }

                case D -> {
                    if (snake.getDirection() != Direction.LEFT)
                        snake.setDirection(Direction.RIGHT);
                }
            }
        }
        changed = true;
    }

    private void drawSnakes(Snake snake, GraphicsContext gc) {
        while (snake != null) {
            int xPos = snake.getX() * 30;
            int yPos = snake.getY() * 30;
            gc.fillRect(xPos, yPos, 30, 30);
            snake = snake.getPreviousSnake();
        }

    }

    private void initYlines(GraphicsContext gc) {
        for (int i = 0; i <= 600; i += 30) {
            gc.beginPath();
            gc.moveTo(i,0);
            gc.lineTo(i,600);
            gc.stroke();
        }
    }

    private void initXlines(GraphicsContext gc) {

        for (int i = 0; i <= 600; i += 30) {
            gc.beginPath();
            gc.moveTo(0,i);
            gc.lineTo(600,i);
            gc.stroke();
        }
    }
}

