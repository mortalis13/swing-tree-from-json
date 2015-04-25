import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class TreeViewerFrame {
  
  public static TreeViewerFrame window;
  
  private JFrame frame;
  public JButton btnNewButton;
  public JTextField tfState;
  public TreeViewer treeViewer;
  
  public DirectoryTree tree;
  public JScrollPane scrollPane;
  
  public JTree justTree;
  public JTree tree_1;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          window = new TreeViewerFrame();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  
  private void addKeyboardManager(){
    KeyboardFocusManager keyManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    keyManager.addKeyEventDispatcher( new KeyEventDispatcher() {
      
      @Override
      public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getID()==KeyEvent.KEY_PRESSED && e.getKeyCode()==27){                              // ESC
          SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
              frame.dispose();
            }
          } );
          return true;
        }
        return false;
      }
    });
  }

  /**
   * Create the application.
   */
  public TreeViewerFrame() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    addKeyboardManager();
    
    frame = new JFrame();
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        treeViewer=new TreeViewer();
      }
    });
    frame.setBounds(100, 100, 577, 503);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GridBagLayout gbl = new GridBagLayout();
    gbl.rowWeights = new double[]{1.0, 0.0, 0.0};
    gbl.columnWeights = new double[]{1.0};
    // gbl.rowWeights = new double[]{0.0, 0.0};
    frame.getContentPane().setLayout(gbl);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 0;
    frame.getContentPane().add(scrollPane, gbc_scrollPane);
    
    tfState = new JTextField();
    GridBagConstraints gbc_tfState = new GridBagConstraints();
    gbc_tfState.insets = new Insets(5, 5, 5, 5);
    gbc_tfState.ipady = 5;
    gbc_tfState.ipadx = 5;
    gbc_tfState.fill = GridBagConstraints.HORIZONTAL;
    gbc_tfState.gridx = 0;
    gbc_tfState.gridy = 1;
    frame.getContentPane().add(tfState, gbc_tfState);
    
    btnNewButton = new JButton("New button");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        treeViewer.showTree();
      }
    });
    
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.ipady = 10;
    gbc_btnNewButton.ipadx = 5;
    gbc_btnNewButton.insets = new Insets(5, 5, 5, 5);
    gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnNewButton.gridx = 0;
    gbc_btnNewButton.gridy = 2;
    frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
  }

}
