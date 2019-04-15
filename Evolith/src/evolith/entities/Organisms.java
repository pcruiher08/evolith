package evolith.entities;

import evolith.game.Game;
import evolith.menus.Hover;
import evolith.game.Item;
import evolith.helpers.SwarmMovement;
import evolith.helpers.Time;
import evolith.engine.Assets;
import evolith.entities.Resources.Plant;
import evolith.helpers.Commons;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Erick González
 * @author Carlos Estrada
 * @author Víctor Villarreal
 * @author Moisés Fernández
 */
public class Organisms implements Commons {

    private ArrayList<Organism> organisms;  //array of all organisms
    private int amount;         //max organism amount

    private Game game;          // game instance
    private int counter;        //frame counter

    private Hover h;            // hover panel
    private boolean hover;      // to know if hovering

    private int newX;           // new x position of the organisms
    private int newY;           // new y position of the organisms
    
    private int skin;
    
    private ArrayList<Point> currentPoss;
    
    private Point centralPoint;
    private Point targetPoint;

    /**
     * Constructor of the organisms
     *
     * @param game
     */
    public Organisms(Game game) {
        this.game = game;
        organisms = new ArrayList<>();
        amount = 1;

        for (int i = 0; i < amount; i++) {
            organisms.add(new Organism(INITIAL_POINT, INITIAL_POINT, ORGANISM_SIZE, ORGANISM_SIZE));
        }

        newX = INITIAL_POINT;
        newY = INITIAL_POINT;
        
        centralPoint = new Point(INITIAL_POINT, INITIAL_POINT);
        targetPoint = new Point(INITIAL_POINT, INITIAL_POINT);
        currentPoss = SwarmMovement.getPositions(500, 500, 50, 1);
    }
    
    /**
     * updates all organisms
     */
    public void tick() {
        for (int i = 0; i < amount; i++) {
            organisms.get(i).tick();
            checkReproduce(organisms.get(i));
            checkKill(organisms.get(i));
        }

        //check the hover
        checkHover();
    }
    
    /**
     * Perform action on mouse clicked
     * @param x
     * @param y 
     */
    public void applyMouse(int x, int y) {
        moveSwarm(x, y);
    }
    
    /**
     * To move the entire swarm to the x and y given
     * @param x
     * @param y 
     */
    public void moveSwarm(int x, int y) {
        ArrayList<Point> points;
        //if left clicked move the organisms to determined point
        
        centralPoint = new Point(x, y);

        points = SwarmMovement.getPositions(centralPoint.x - ORGANISM_SIZE /2, centralPoint.y - ORGANISM_SIZE /2, amount);
        for (int i = 0; i < amount; i++) {
            organisms.get(i).setPoint(points.get(i));
        }
    }
    
    /**
     * to move the swarm to the specified coordinates given there is an object in the middle
     * @param x
     * @param y 
     * @param obj 
     */
    public void moveSwarm(int x, int y, int obj) {
        ArrayList<Point> points;
        //if left clicked move the organisms to determined point
        
        centralPoint = new Point(x, y);

        points = SwarmMovement.getPositions(centralPoint.x - ORGANISM_SIZE /2, centralPoint.y - ORGANISM_SIZE /2, amount, obj);
        for (int i = 0; i < amount; i++) {
            organisms.get(i).setPoint(points.get(i));
        }
    }
    
    public void moveSwarmToPoint(int x, int y, int obj) {
        Point p = new Point(x, y);
        
        for (int i = 0; i < amount; i++) {
            organisms.get(i).setPoint(p);
        }
    }

    /**
     * To check the hover panel over an organism
     */
    private void checkHover() {

        for (int i = 0; i < amount; i++) {
            //if mouse is countained in a certain organism
            if (organisms.get(i).getPerimeter().contains(game.getCamera().getAbsX(game.getMouseManager().getX()),
                    game.getCamera().getAbsY(game.getMouseManager().getY()))) {
                 //sets new hover panel with that organism's location and information
                h = new Hover(game.getMouseManager().getX(), game.getMouseManager().getY(), 170, 220,
                        organisms.get(i).hunger, organisms.get(i).thirst, organisms.get(i).maturity, game);
                //activates the hover
                setHover(true);
                break;
            } else {
                setHover(false);
            }
        }

    }
    
    /**
     * Check if individual organism needs reproduction
     * @param org 
     */
    private void checkReproduce(Organism org) {
        if (org.isNeedOffspring()) {
            org.setNeedOffspring(false);
            amount++;
            organisms.add(new Organism(org.getX()+ORGANISM_SIZE, org.getY(), ORGANISM_SIZE, ORGANISM_SIZE));
        }
    }
    
    /**
     * Check if an organism needs to be killed
     * @param org 
     */
    private void checkKill(Organism org) {
        if (org.isDead()) {
            organisms.remove(org);
            amount--;
        }
    }
    /*
    public void checkProximity(Plants plants) {
        for (int i = 0; i < amount; i++) {
            if (plants.checkRadius(organisms.get(i).getRadius(),i) && !organisms.get(i).isInPlant()) {
                System.out.println("CLOSE");
                organisms.get(i).setPoint(currentPoss.get(0));
                organisms.get(i).setInPlant(true);
                currentPoss.remove(0);
            }
        }
    }*/
    
    public void checkOnResource(Resources resources) {
        for (int i = 0; i < amount; i++) {
            if (resources.assignOnResource(organisms.get(i).getPerimeter())) {
                organisms.get(i).setPoint(currentPoss.get(0));
                organisms.get(i).setInResource(true);
                currentPoss.remove(0);
            }
        }
    }
    
    public void setResource(Item item) {
        for (int i = 0; i < amount; i++) {
            organisms.get(i).setTarget(item);
        }
    }
    /*
    public void checkProximity(Waters waters) {
        for (int i = 0; i < amount; i++) {
            if (waters.checkRadius(organisms.get(i).getRadius(),i) && !organisms.get(i).isInWater()) {
                System.out.println("CLOSE");
                organisms.get(i).setPoint(currentPoss.get(0));
                organisms.get(i).setInWater(true);
                currentPoss.remove(0);
            }
        }
    }*/

    /**
     * To render the organisms
     *
     * @param g
     */
    public void render(Graphics g) {

        for (int i = 0; i < amount; i++) {
            organisms.get(i).render(g);
        }
        //render the hover panel of an organism
        if (h != null && isHover()) {
            h.render(g);
        }
    }

    /**
     * To set the hover status
     *
     * @param hover
     */
    public void setHover(boolean hover) {
        this.hover = hover;
    }

    /**
     * To know if hover is active
     *
     * @return hover
     */
    public boolean isHover() {
        return hover;
    }
    
    /**
     * Set the skin of the organisms
     * @param skin 
     */
    public void setSkin(int skin) {
        this.skin = skin;
    }
    
    /**
     * Get the skin <code>int</code> used by the organisms
     * @return 
     */
    public int getSkin() {
        return skin;
    }
    
    /**
     * to set the central point of the swarm
     * @param centralPoint 
     */
    public void setCentralPoint(Point centralPoint) {
        this.centralPoint = centralPoint;
    }
    
    /**
     * to get the central point of the swarm
     * @return 
     */
    public Point getCentralPoint() {
        return centralPoint;
    }
    
    public void setSearchFood(boolean val) {
        for (int i = 0; i < amount; i++) {
            organisms.get(i).setSearchFood(val);
        }
    }
    
    public void setSearchWater(boolean val) {
        for (int i = 0; i < amount; i++) {
            organisms.get(i).setSearchWater(val);
        }
    }
    
    public void checkIfTargetValid(Resources resources) {
        for (int i = 0; i < amount; i++) {
            if (((Plant)organisms.get(i).getTarget()).isFull()) {
                if (organisms.get(i).isSearchFood()) {
                    findNearestValidFood(organisms.get(i), resources);
                }
            }
        }
    }
    
    public void findNearestValidFood(Organism org, Resources resources) {
        Plant closestPlant = resources.getPlant(0); 
        double closestDistanceBetweenPlantAndOrganism = Math.sqrt(Math.pow(org.getX()-resources.getPlant(0).getX(),2) + Math.pow(org.getY()-resources.getPlant(0).getY(),2) );
        for(int i = 1; i<resources.getPlantsAmount(); i++){
            double distanceBetweenPlantAndOrganism = 7072;
            if(!resources.getPlant(i).isFull()){
                distanceBetweenPlantAndOrganism = Math.sqrt(Math.pow(org.getX()-resources.getPlant(i).getX(),2) + Math.pow(org.getY()-resources.getPlant(i).getY(),2) );
            }
            
            if(distanceBetweenPlantAndOrganism<closestDistanceBetweenPlantAndOrganism){
                closestDistanceBetweenPlantAndOrganism = distanceBetweenPlantAndOrganism;
                closestPlant = resources.getPlant(i);
            }
        }
        org.setTarget(closestPlant);
    }

    /**
     * Single organism class
     */
    public class Organism extends Item {

        private Point point;
        private int maxVel;
        private int acc;
        private int xVel;
        private int yVel;

        private Time time;
        
        /**
         * These are the five evolutionary traits
         */
        private int size;
        private int speed;
        private int strength;
        private int stealth;
        private int survivability;
        
        private int life;           //Health points of the organism
        private int hunger;         //hunger of the organism
        private int thirst;         //thirst of the organism
        private int maturity;       //maturity level of the organsim
        
        private int prevHungerRed; //Time in seconds at which hunger was previously reduced
        private int prevThirstRed; //Time in seconds at which hunger was previously reduced
        private int prevMatInc; //Time in seconds at which maturity was previously increased
        
        private boolean needOffspring;
        private boolean dead;
        private boolean moving;
        private boolean inPlant;
        private boolean inWater;
        private boolean inResource;
        
        private Item target;
        
        private boolean searchFood;
        private boolean searchWater;

        /**
         * Constructor of the organism
         *
         * @param x
         * @param y
         * @param width
         * @param height
         */
        public Organism(int x, int y, int width, int height) {
            super(x, y, width, height);
            point = new Point(x, y);
            maxVel = 3;
            xVel = 0;
            yVel = 0;
            acc = 1;

            size = 1;
            speed = 1;
            strength = 1;
            stealth = 1;
            survivability = 1;
            
            life = 100;
            hunger = 100;
            thirst = 100;
            maturity = 0;
            
            prevHungerRed = 0;
            prevThirstRed = 0;
            prevMatInc = 0;
            
            needOffspring = false;
            dead = false;
            inPlant = false;
            inWater = false;
            inResource = false;
            
            searchFood = false;
            searchWater = false;

            time = new Time();
        }
        
        /**
         * To tick the organism
         */
        @Override
        public void tick() {
            //to determine the lifespan of the organism
            time.tick();
            checkMovement();
            checkVitals();
            
            radius.setX(x);
            radius.setY(y);
        }
        
        /**
         * Update the position of the organism accordingly
         */
        private void checkMovement() {
            // if the organism is less than 25 units reduce velocity
            if (Math.abs((int) point.getX() - x) < 15 && Math.abs((int) point.getY() - y) < 25) {
                // if the organism is less than 15 units reduce velocity
                if (Math.abs((int) point.getX() - x) < 15 && Math.abs((int) point.getY() - y) < 15) {
                    // if the organism is less than 5 units reduce velocity
                    if (Math.abs((int) point.getX() - x) < 5 && Math.abs((int) point.getY() - y) < 5) {
                        moving = false;
                        maxVel = 0;
                    } else {
                        moving = true;
                        maxVel = 1;
                    }
                } else {
                    moving = true;
                    maxVel = 2;
                }
            } else {
                moving = true;
                maxVel = 3;
            }

            //move in the x to the point
            if ((int) point.getX() > x) {
                xVel += acc;
            } else {
                xVel -= acc;
            }
            //move in the y to the point
            if ((int) point.getY() > y) {
                yVel += acc;
            } else {
                yVel -= acc;
            }
            //limits the x velocity
            if (xVel > maxVel) {
                xVel = maxVel;
            }

            if (xVel < maxVel * -1) {
                xVel = maxVel * -1;
            }
            //limits the y velocity
            if (yVel > maxVel) {
                yVel = maxVel;
            }

            if (yVel < maxVel * -1) {
                yVel = maxVel * -1;
            }
            //increments the velocity
            x += xVel;
            y += yVel;
        }
        
        /**
         * To check the update and react to the vital stats of the organism
         */
        private void checkVitals() {
            //Reduce hunger every x seconds defined in the commmons class
            if (time.getSeconds() >= prevHungerRed + SECONDS_PER_HUNGER) {
                hunger--;
                prevHungerRed = (int) time.getSeconds();
            }
            
            //Reduce thirst every x seconds defined in the commmons class
            if (time.getSeconds() >= prevThirstRed + SECONDS_PER_THIRST) {
                thirst--;
                prevThirstRed = (int) time.getSeconds();
            }
            
            //Increase maturity every x seconds defined in the commmons class
            if (time.getSeconds() >= prevMatInc + SECONDS_PER_MATURITY) {
                maturity++;
                prevMatInc = (int) time.getSeconds();
                
                //Reproduction happen at these two points in maturity
                if (maturity == 23) {
                    needOffspring = true;
                }
                
                if (maturity == 26) {
                    needOffspring = true;
                }
            }
            
            //Once the organisms reaches max maturity, kill it
            if (maturity >= MAX_MATURITY) {
                kill();
            }
        }
        
        /**
         * Kill the organism
         */
        public void kill() {
            dead = true;
        }

        /**
         * Renders the organisms relative to the camera
         *
         * @param g
         */
        @Override
        public void render(Graphics g) {
            g.drawImage(Assets.orgColors.get(skin), game.getCamera().getRelX(x), game.getCamera().getRelY(y), width, height, null);
            g.setColor(Color.RED);
            g.drawOval(game.getCamera().getRelX(radius.getX() - width / 2), game.getCamera().getRelY(radius.getY() - width / 2), radius.getRadius(), radius.getRadius());
        }

        /**
         * To get the point
         *
         * @return
         */
        public Point getPoint() {
            return point;
        }

        /**
         * To set the point
         *
         * @param point
         */
        public void setPoint(Point point) {
            this.point = point;
        }
        
        /**
         * To get needOffspring
         * @return needOffspring
         */
        public boolean isNeedOffspring() {
            return needOffspring;
        }
        
        /**
         * To set needOffspring
         * @param needOffspring 
         */
        public void setNeedOffspring(boolean needOffspring) {
            this.needOffspring = needOffspring;
        }
        
        /**
         * To check if the organism is dead
         * @return dead
         */
        public boolean isDead() {
            return dead;
        }
        
        /**
         * To set dead
         * @param dead 
         */
        public void setDead(boolean dead) {
            this.dead = dead;
        }

        public boolean isMoving() {
            return moving;
        }

        public void setMoving(boolean moving) {
            this.moving = moving;
        }

        public boolean isInPlant() {
            return inPlant;
        }

        public void setInPlant(boolean inPlant) {
            this.inPlant = inPlant;
        }
        
        public boolean isInWater(){
            return inWater;
        }
        
        public void setInWater(boolean inWater){
            this.inWater = inWater;
        }
        
        public boolean isInResource(){
            return inResource;
        }
        
        public void setInResource(boolean inResource){
            this.inResource = inResource;
        }

        public Item getTarget() {
            return target;
        }

        public void setTarget(Item target) {
            this.target = target;
        }

        public boolean isSearchFood() {
            return searchFood;
        }

        public boolean isSearchWater() {
            return searchWater;
        }

        public void setSearchFood(boolean searchFood) {
            this.searchFood = searchFood;
        }

        public void setSearchWater(boolean searchWater) {
            this.searchWater = searchWater;
        }
    }
}