package evolith;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Erick González
 * @author Carlos Estrada
 * @author Víctor Villarreal
 * @author Moisés Fernández
 */
public class Plants implements Commons {

    private ArrayList<Plant> plants;    // arrays of the plants 
    private Game game;                  //game instance

    private int amount;                 //max amount of the plant

    /**
     * Constructor of the plants in the game
     *
     * @param game
     */
    public Plants(Game game) {
        this.game = game;
        plants = new ArrayList<>();
        amount = PLANTS_AMOUNT;

        for (int i = 0; i < amount; i++) {
            plants.add(new Plant(400, 400, PLANT_SIZE, PLANT_SIZE));
        }
    }

    /**
     * To tick the plants
     */
    public void tick() {
        for (int i = 0; i < amount; i++) {
            plants.get(i).tick();
        }
    }

    /**
     * To render the plants
     *
     * @param g
     */
    public void render(Graphics g) {
        for (int i = 0; i < amount; i++) {
            plants.get(i).render(g);
        }
    }

    private class Plant extends Item {

        int quantity;   // maximum quanitity of food per plant

        /**
         * Constructor of a new plant
         *
         * @param x
         * @param y
         * @param width
         * @param height
         */
        public Plant(int x, int y, int width, int height) {
            super(x, y, width, height);
            quantity = 100;
        }

        /**
         * To tick the plant
         */
        @Override
        public void tick() {
        }

        /**
         * Renders the plant
         *
         * @param g
         */
        @Override
        public void render(Graphics g) {
            g.setColor(new Color(173, 255, 250));
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

            g.drawImage(Assets.plant, game.getCamera().getRelX(x), game.getCamera().getRelY(y), width, height, null);

            //To display the actual quantity over the maximum
            g.drawString(Integer.toString(quantity) + "/100", game.getCamera().getRelX(x) + 45, game.getCamera().getRelY(y) + 150);
        }

        /**
         * To get the quantity of the plant
         *
         * @return quantity
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * To set the quantity of the plant
         *
         * @param quantity
         */
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
