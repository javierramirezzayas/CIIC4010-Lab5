import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	private Random generator = new Random();
	private Color oldColor = Color.BLUE; //Dummy Color

	public void mousePressed(MouseEvent e) {
		
		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame) c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
		Insets myInsets = myFrame.getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		e.translatePoint(-x1, -y1);
		int x = e.getX();
		int y = e.getY();
		myPanel.x = x;
		myPanel.y = y;
		myPanel.mouseDownGridX = myPanel.getGridX(x, y);
		myPanel.mouseDownGridY = myPanel.getGridY(x, y);
		myPanel.repaint();
		
		switch (e.getButton()) {
		case 1:		//Left mouse button
			break;
			
		case 3:		//Right mouse button
			if ((myPanel.mouseDownGridX == 0) || (myPanel.mouseDownGridY == 0)){
				//Pressed mouse on cells
				//Do Nothing
			}
			break;
			
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {

		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame)c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
		Insets myInsets = myFrame.getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		e.translatePoint(-x1, -y1);
		int x = e.getX();
		int y = e.getY();
		myPanel.x = x;
		myPanel.y = y;
		int gridX = myPanel.getGridX(x, y);
		int gridY = myPanel.getGridY(x, y);

		switch (e.getButton()) {
		case 1:		//Left mouse button

			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} 
			else {
				if ((myPanel.mouseDownGridX == 0) || (myPanel.mouseDownGridY == 0)){
					//Pressed the mouse button on a gray cell and Released the mouse button on a different cell where it was pressed
					//Do Nothing
				}
				else {
					if ((gridX == -1) || (gridY == -1)) {
						//Pressed the mouse button and Is releasing outside
						myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = compareColor(colorSelect());
						myPanel.repaint();
					} 
					else{
						if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
							//Pressed the mouse button on a white cell and Released the mouse button on a different cell where it was pressed
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = compareColor(colorSelect());
							myPanel.repaint();
						} 
						else {
							//Released the mouse button on the same cell where it was pressed
							if ((gridX == 0) || (gridY == 0)) {
								//On the left column and on the top row... do nothing
							} 
							else {
								//On the grid other than on the left column and on the top row:
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = compareColor(colorSelect());
								myPanel.repaint();
							}
						}
					}
				}
			}

			if((myPanel.mouseDownGridX == 0) && (myPanel.mouseDownGridY == gridY && gridY > 0)){
				//Pressed the mouse buttons on gray cells on the leftmost column ... Paint Row
				int j=1;
				for(int i = 0; i < 9; i++){
					myPanel.colorArray[myPanel.mouseDownGridX + j][myPanel.mouseDownGridY] = compareColor(colorSelect());
					myPanel.repaint();
					j++;
				}
			}
			else{
				if((myPanel.mouseDownGridX == gridX && gridX > 0) && (myPanel.mouseDownGridY == 0)){
					//Pressed the mouse buttons on gray cells on the top ... Paint Column
					int k=1;
					for(int i = 0; i < 9; i++){
						myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY + k] = compareColor(colorSelect());
						myPanel.repaint();
						k++;
					}
				}
				else{
					if((myPanel.mouseDownGridX == 0) && (myPanel.mouseDownGridY == 0)){
						//Pressed the mouse buttons on the left column and on the top row... Paint Diagonally
						int j=1;
						int k=1;
						for(int i = 0; i < 9; i++){
							myPanel.colorArray[myPanel.mouseDownGridX + j][myPanel.mouseDownGridY + k] = compareColor(colorSelect());
							myPanel.repaint();
							j++; k++;
						}
					}
				}
			}

			if ((myPanel.mouseDownGridX == 0 && gridX == 0) && (myPanel.mouseDownGridY == 10 && gridY == 10)) {
				//Pressed the button on the bottom left gray cell ... Paint Center
				int j=4;
				int k=4;

				for(int i=0; i<3; i++){
					for(int m=0; m<3; m++){
						myPanel.colorArray[j][k] = compareColor(colorSelect());
						myPanel.repaint();
						oldColor = myPanel.colorArray[j][k];
						k++;
					}
					k=4;
					j++;
				}
			}
			myPanel.repaint();
			break;

		case 3:		//Right mouse button	
			if((myPanel.mouseDownGridX == -1 && gridX == -1) && (myPanel.mouseDownGridY == -1 && gridY ==-1)){
				//Pressed mouse outside the cells
				int j=0;
				int k=0;
				Color oldColor = Color.RED;

				for(int m=0; m<11; m++){
					myPanel.colorArray[j][k] = colorSelectRightClick(oldColor);
					myPanel.repaint();
					oldColor = myPanel.colorArray[j][k];
					k++;
				}

				k=0;

				for(int i=0; i<10; i++){
					myPanel.colorArray[j][k] = colorSelectRightClick(oldColor);
					myPanel.repaint();
					oldColor = myPanel.colorArray[j][k];
					j++;
				}
			}
			
			break;

		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}

	public Color colorSelect(){
		Color newColor = null;
		switch (generator.nextInt(5)) {
		case 0:
			newColor = Color.YELLOW;
			break;
		case 1:
			newColor = Color.MAGENTA;
			break;
		case 2:
			newColor = Color.BLACK;
			break;
		case 3:
			newColor = new Color(0x964B00);   //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
			break;
		case 4:
			newColor = new Color(0xB57EDC);   //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
			break;
		}
		return newColor;
	}


	public Color compareColor(Color oldColor){
		Color newColor = Color.WHITE;

		do{
			switch (generator.nextInt(5)) {
			case 0:
				newColor = Color.YELLOW;
				break;
			case 1:
				newColor = Color.MAGENTA;
				break;
			case 2:
				newColor = Color.BLACK;
				break;
			case 3:
				newColor = new Color(0x964B00);   //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
				break;
			case 4:
				newColor = new Color(0xB57EDC);   //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
				break;
			}
		}while(newColor.equals(this.oldColor));

		this.oldColor = newColor;

		return newColor;
	}

	public Color colorSelectRightClick(Color oldColor){
		Color newColor = null;

		do{
			switch (generator.nextInt(3)) {
			case 0:
				newColor = Color.BLUE;
				break;
			case 1:
				newColor = Color.RED;
				break;
			case 2:
				newColor = Color.GREEN;
				break;
			}
		}while(oldColor.equals(newColor));

		return newColor;

	}

}


