import java.awt.*;
import javax.swing.*;

/** Simple example illustrating the use of JButton, especially
 *  the new constructors that permit you to add an image.
 *  1998-99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class JButtons extends JFrame {
  public static void main(String[] args) {
    new JButtons();
  }

  public JButtons() {
    super("Using JButton");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();
    content.setBackground(Color.white);
    content.setLayout(new FlowLayout());
    JButton button1 = new JButton("Java");
    content.add(button1);
    ImageIcon cup = new ImageIcon("../images/rArrow.jpg");
    JButton button2 = new JButton(cup);
button2.setBackground(Color.BLACK);
    content.add(button2);
    JButton button3 = new JButton(cup);
    content.add(button3);
    JButton button4 = new JButton("Java", cup);
    button4.setHorizontalTextPosition(SwingConstants.LEFT);
    content.add(button4);
    pack();
    setVisible(true);
  }
}
