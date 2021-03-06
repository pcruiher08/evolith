/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolith.entities;

import evolith.engine.Assets;
import evolith.game.Game;
import evolith.game.Item;
import evolith.helpers.Commons;
import evolith.helpers.SwarmMovement;
import evolith.helpers.Time;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

/**
 *
 * @author charles
 */
public class Resource extends Item implements Commons{

    private int quantity;
    private Game game;
    private boolean full;
    private boolean over;
    private int parasiteAmount;
    private final ArrayList<Point> positions;
    private HashMap<Organism, Integer> map;
    private Time time;

    public enum ResourceType {Plant, Water};
    private ResourceType type;
    
    private int prevSecUpdate;
    
    public Resource(int x, int y, int width, int height, Game game, ResourceType type) {
        super(x, y, width, height);
        this.game = game;
        quantity = 100;
        full = false;
        over = false;
        parasiteAmount = 0;
        map = new HashMap<>();
        positions = SwarmMovement.getPositions(x + PLANT_SIZE / 2, y + PLANT_SIZE / 2, 6, 1);
        
        time = new Time();
        prevSecUpdate = 0;
        
        this.type = type;
    }
    
    public void addParasite(Organism org) {
        if (!full) {
            for (int i = 0; i < 6; i++) {
                if (!map.containsValue(i)) {
                    map.put(org, i);
                    org.setPoint((Point) positions.get(i).clone());
                    //System.out.println(positions.get(i));
                    //System.out.println("TO ID:   " + org.getId());
                    parasiteAmount++;
                    if (parasiteAmount >= 6) {
                        full = true;
                    }
                    return;
                }
            }
            
            //If code reaches here, it is already full so error
            System.out.println("ERROR, POSITIONS FULL");
        }
    }
    
    public void removeParasite(Organism org, int i) {
        if (map.containsKey(org)) {
            //System.out.println("AMOUNT  :" + map.size());
            map.remove(org);
            parasiteAmount--;
            if (parasiteAmount < 6) {
                full = false;
            }
            //System.out.println("PARASITE REMOVED  ID:  " + i);
        } else {
            System.out.println("ERROR, ORGANISM NOT IN RESOURCE  ID:  " + i);
        }
        
        //System.out.println("END OF REMOVEPAR FUNCTION:  ID:   " + i);
    }
    
    public void removeParasites() {
        map.clear();
    }
    
    boolean hasParasite(Organism org) {
        return map.containsKey(org);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }
    
    @Override
    public void tick() {
        time.tick();
        
        if (time.getSeconds() > prevSecUpdate + CONSUMING_RATE) {
            quantity -= parasiteAmount;
            prevSecUpdate = (int) time.getSeconds();
            Iterator it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry element = (Map.Entry) it.next();
                Organism org = (Organism) element.getKey();
                if(org.isEating()){
                    int actualHunger = org.getHunger();
                    org.setHunger(actualHunger+=2);
                }
                if(org.isDrinking()){
                    int actualThirst = org.getThirst();
                    org.setThirst(actualThirst+=2);
                }
            }
        }
        
        if (quantity <= 0) {
            quantity = 0;
            over = true;
        }

    }

    @Override
    public void render(Graphics g) {
        switch(type) {
            case Plant:
                g.setColor(new Color(173, 255, 250));
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

                g.drawImage(Assets.plant, game.getCamera().getRelX(x), game.getCamera().getRelY(y), width, height, null);
               
                //To display the actual quantity over the maximum
                g.drawString(Integer.toString(quantity) + "/100", game.getCamera().getRelX(x) + 45, game.getCamera().getRelY(y) + 150);
                break;
            case Water:
                g.setColor(new Color(173, 255, 250));
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

                g.drawImage(Assets.water, game.getCamera().getRelX(x), game.getCamera().getRelY(y), width, height, null);

                //To display the actual quantity over the maximum
                g.drawString(Integer.toString(quantity) + "/100", game.getCamera().getRelX(x) + 45, game.getCamera().getRelY(y) + 150);
        }
    }
}
