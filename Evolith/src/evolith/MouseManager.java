
package evolith;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 *
 * @author Erick González
 * @author Carlos Estrada
 * @author Víctor Villarreal
 * @author Moisés Fernández
 */
public class MouseManager implements MouseListener, MouseMotionListener {

    private boolean izquierdo;
    private boolean derecho;
    private int x;
    private int y;
    
    public MouseManager() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
       if (e.getButton() == MouseEvent.BUTTON1) {
            izquierdo = true;
            x = e.getX();
            y = e.getY();   
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            izquierdo = false; 
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (true) {
            izquierdo = true;
            x = e.getX();
            y = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(true){
            x = e.getX();
            y = e.getY();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isIzquierdo() {
        return izquierdo;
    }
    
    public boolean isDerecho() {
        return derecho;
    }

    public void setIzquierdo(boolean izquierdo) {
        this.izquierdo = izquierdo;
    }
}
