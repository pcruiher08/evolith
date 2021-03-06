package evolith.menus;

import evolith.game.Game;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Erick González
 * @author Carlos Estrada
 * @author Víctor Villarreal
 * @author Moisés Fernández
 */
public abstract class Menu {

    protected int x; //x position
    protected int y; //y position
    protected int width;
    protected int height;
    protected Game game;
    
    protected ArrayList<Button> buttons; //Buttons in the menu

    /**
     * To create a new menu
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Menu(int x, int y, int width, int height, Game game) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.game = game;
        buttons = new ArrayList<Button>();
    }

    /**
     * To get a rectangle object with current position and size
     *
     * @return
     */
    public Rectangle getPerimeter() {
        return new Rectangle(x, y, width, height);
    }
    
    /**
     * Check if the menu contains the mouse given as coordinates
     * @param x
     * @param y
     * @return 
     */
    public boolean hasMouse(int x, int y) {
        return getPerimeter().contains(x, y);
    } 

    /**
     * To get x
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * To get y
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * To set x
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * to set y
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * to get width
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * to set width
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * to set height
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * to set width
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * To tick the menu
     */
    public abstract void tick();

    /**
     * To render the menu
     *
     * @param g
     */
    public abstract void render(Graphics g);
}
