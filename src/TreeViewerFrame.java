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
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;


public class TreeViewerFrame {
  
  public static TreeViewerFrame window;
  
  private JFrame frame;
  public JButton bLoadTree;
  public JTextField tfPath;
  public TreeViewer treeViewer;
  
  public DirectoryTree tree;
  public JScrollPane scrollPane;
  
  public JLabel lPath;
  public JButton bBrowse;
  public JFileChooser fc;
  
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
    frame.setBounds(100, 100, 577, 535);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GridBagLayout gbl = new GridBagLayout();
    gbl.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0};
    gbl.columnWeights = new double[]{1.0};
    // gbl.rowWeights = new double[]{0.0, 0.0};
    frame.getContentPane().setLayout(gbl);
    
    lPath = new JLabel();
    lPath.setBorder(new EmptyBorder(0, 5, 0, 0));
    lPath.setText("Path");
    GridBagConstraints gbc_lPath = new GridBagConstraints();
    gbc_lPath.insets = new Insets(5, 5, 0, 5);
    gbc_lPath.fill = GridBagConstraints.HORIZONTAL;
    gbc_lPath.gridx = 0;
    gbc_lPath.gridy = 0;
    frame.getContentPane().add(lPath, gbc_lPath);
    
    tfPath = new JTextField();
    tfPath.setMargin(new Insets(5, 5, 2, 5));
    GridBagConstraints gbc_tfPath = new GridBagConstraints();
    gbc_tfPath.ipady = 5;
    gbc_tfPath.insets = new Insets(5, 5, 5, 5);
    gbc_tfPath.ipadx = 5;
    gbc_tfPath.fill = GridBagConstraints.HORIZONTAL;
    gbc_tfPath.gridx = 0;
    gbc_tfPath.gridy = 1;
    frame.getContentPane().add(tfPath, gbc_tfPath);
    
    bBrowse = new JButton();
    bBrowse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fc = new JFileChooser(".");
        fc.setFileFilter(new CustomFilter());
        int returnVal = fc.showOpenDialog(frame);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          String path=file.getPath();
          path=TreeViewer.formatPath(path);
          tfPath.setText(path);
        } 
      }
    });
    bBrowse.setText("Browse...");
    GridBagConstraints gbc_bBrowse = new GridBagConstraints();
    gbc_bBrowse.insets = new Insets(5, 0, 5, 5);
    gbc_bBrowse.fill = GridBagConstraints.HORIZONTAL;
    gbc_bBrowse.gridx = 1;
    gbc_bBrowse.gridy = 1;
    gbc_bBrowse.ipady = 3;
    frame.getContentPane().add(bBrowse, gbc_bBrowse);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 2;
    gbc_scrollPane.gridwidth = 2;
    frame.getContentPane().add(scrollPane, gbc_scrollPane);
    
    bLoadTree = new JButton("Load Tree");
    bLoadTree.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        treeViewer.showTree();
      }
    });
    
    GridBagConstraints gbc_bLoadTree = new GridBagConstraints();
    gbc_bLoadTree.ipady = 20;
    gbc_bLoadTree.ipadx = 5;
    gbc_bLoadTree.insets = new Insets(5, 5, 5, 5);
    gbc_bLoadTree.fill = GridBagConstraints.HORIZONTAL;
    gbc_bLoadTree.gridx = 0;
    gbc_bLoadTree.gridy = 3;
    gbc_bLoadTree.gridwidth = 2;
    frame.getContentPane().add(bLoadTree, gbc_bLoadTree);
  }

}
