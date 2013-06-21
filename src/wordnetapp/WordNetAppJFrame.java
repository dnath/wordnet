package wordnetapp;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;

import net.didion.jwnl.data.POS;

public class WordNetAppJFrame extends JFrame {	
	private static final long serialVersionUID = 4048914114525668680L;
	private static final Logger logger = Logger.getLogger(new RuntimeException()
												.getStackTrace()[0].getClassName());

	public WordNetAppJFrame() {
        initComponents();
        addAction();
		mBean = new JWNLBean();
    } 
	
	public WordNetAppJFrame(String fileProperties) {
        initComponents();
        addAction();
		mBean = new JWNLBean(fileProperties);
    } 
   
	private void addAction() {
        jTextFieldSearchItem.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    logger.debug("ENTER pressed");
                    searchWordNet();
                }
            }

            public void keyReleased(KeyEvent e) { }
        });

        mSearch = new JMenuItem("Search with WordNet");
        mSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String word = jTextArea1.getSelectedText();
                jTextFieldSearchItem.setText(word);
                jRadioButtonAll.setSelected(true);
                if(!word.trim().equals(""))
                    jTextArea1.setText(mBean.findAllSynsets(word));
            }
        });
        
        jPopupMenu1.add(mSearch);
        jPopupMenu1.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
            public void popupMenuCanceled(PopupMenuEvent e) { }
        });
        
        jTextArea1.add(jPopupMenu1);
        jTextArea1.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                checkPopup(e);
            }
            public void mousePressed(MouseEvent e) {
                checkPopup(e);
            }
            public void mouseReleased(MouseEvent e) {
                checkPopup(e);
            }
            public void mouseEntered(MouseEvent e) {
                checkPopup(e);
            }
            public void mouseExited(MouseEvent e) {
                checkPopup(e);
            }

            private void checkPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    jPopupMenu1.show(WordNetAppJFrame.this, e.getXOnScreen(), e.getYOnScreen());
                }
            }
        });
		
	}
	
    private void initComponents() {

        buttonGroup1 = new ButtonGroup();
        jPopupMenu1 = new JPopupMenu();
        jTextFieldSearchItem = new JTextField();
        jButtonSearchWordNet = new JButton();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jRadioButtonNoun = new JRadioButton();
        jRadioButtonVerb = new JRadioButton();
        jRadioButtonAdjective = new JRadioButton();
        jRadioButtonAdverb = new JRadioButton();
        jRadioButtonAll = new JRadioButton();
        jButtonHypernym = new JButton();
        jButtonHyponym = new JButton();
        jButtonAntonym = new JButton();
        jButtonMeronym = new JButton();
        jButtonHolonyms = new JButton();
        jButtonCoordTerms = new JButton();
        jButtonEntail = new JButton();
        jButtonCoordEntailedBy = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WordNet Java App - using JWNL");

        jButtonSearchWordNet.setText("Search WordNet");
        jButtonSearchWordNet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonSearchWordNetActionPerformed(event);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jRadioButtonNoun.setText("Noun");
        jRadioButtonVerb.setText("Verb");
        jRadioButtonAdjective.setText("Adjective");
        jRadioButtonAdverb.setText("Adverb");
        jRadioButtonAll.setSelected(true);
        jRadioButtonAll.setText("All");
        
        jButtonHypernym.setText("Hypernyms");
        jButtonHypernym.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonHypernymActionPerformed(event);
            }
        });

        jButtonHyponym.setText("Hyponyms");
        jButtonHyponym.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonHyponymActionPerformed(event);
            }
        });

        jButtonAntonym.setText("Antonyms");
        jButtonAntonym.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonAntonymActionPerformed(event);
            }
        });

        jButtonMeronym.setText("Meronyms");
        jButtonMeronym.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonMeronymActionPerformed(event);
            }
        });

        jButtonHolonyms.setText("Holonyms");
        jButtonHolonyms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonHolonymsActionPerformed(event);
            }
        });

        jButtonCoordTerms.setText("Coord. Terms");
        jButtonCoordTerms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonCoordTermsActionPerformed(event);
            }
        });

        jButtonEntail.setText("Entailment");
        jButtonEntail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonEntailActionPerformed(event);
            }
        });

        jButtonCoordEntailedBy.setText("Entailed By");
        jButtonCoordEntailedBy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jButtonCoordEntailedByActionPerformed(event);
            }
        });
        
        addLayout();
        pack();
        
        this.buttonGroup1.add(jRadioButtonNoun);
        this.buttonGroup1.add(jRadioButtonVerb);
        this.buttonGroup1.add(jRadioButtonAdverb);
        this.buttonGroup1.add(jRadioButtonAdjective);
        this.buttonGroup1.add(jRadioButtonAll);

        this.setResizable(true);

        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
		}
        catch (Exception e){
	        logger.error(e);
	    }

    }
    
    private void addLayout() {
    	GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButtonNoun)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonVerb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonAdjective)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonAdverb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonAll))
                    .addComponent(jTextFieldSearchItem, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButtonSearchWordNet)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonMeronym, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonHypernym, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonHolonyms, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonHyponym, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonCoordTerms, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAntonym, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonCoordEntailedBy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonEntail, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearchItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonEntail, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAntonym, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonHyponym, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonHypernym, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearchWordNet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButtonNoun)
                        .addComponent(jRadioButtonVerb)
                        .addComponent(jRadioButtonAdjective)
                        .addComponent(jRadioButtonAdverb)
                        .addComponent(jRadioButtonAll))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCoordEntailedBy, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCoordTerms, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonHolonyms, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonMeronym, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addContainerGap())
        );
    }

    private void jButtonSearchWordNetActionPerformed(ActionEvent event) {
        searchWordNet();
    }

    private void jButtonHypernymActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findHypernyms(word));
    }

    private void jButtonHyponymActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findHyponyms(word));
    }

    private void jButtonAntonymActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findAntonyms(word));
    }

    private void jButtonMeronymActionPerformed(ActionEvent event) {
		String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findMeronyms(word));
    }

    private void jButtonHolonymsActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findHolonyms(word));
    }

    private void jButtonCoordTermsActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findCoordTerms(word));
    }

    private void jButtonEntailActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findEntail(word));
    }

    private void jButtonCoordEntailedByActionPerformed(ActionEvent event) {
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals(""))
            this.jTextArea1.setText(mBean.findEntailBy(word));
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WordNetAppJFrame().setVisible(true);
            }
        });
    }

    private void searchWordNet(){
        String word = this.jTextFieldSearchItem.getText().trim();
        if(!word.equals("")){
            if(this.jRadioButtonAll.isSelected())
                this.jTextArea1.setText(mBean.findAllSynsets(word));
            else if(this.jRadioButtonNoun.isSelected())
                this.jTextArea1.setText(mBean.findSynsets(word, POS.NOUN));
            else if(this.jRadioButtonVerb.isSelected())
                this.jTextArea1.setText(mBean.findSynsets(word, POS.VERB));
            else if(this.jRadioButtonAdjective.isSelected())
                this.jTextArea1.setText(mBean.findSynsets(word, POS.ADJECTIVE));
            else if(this.jRadioButtonAdverb.isSelected())
                this.jTextArea1.setText(mBean.findSynsets(word, POS.ADVERB));
        }
    }

    private ButtonGroup buttonGroup1;
    private JButton jButtonCoordEntailedBy;
    private JButton jButtonCoordTerms;
    private JButton jButtonEntail;
    private JButton jButtonHolonyms;
    private JButton jButtonHypernym;
    private JButton jButtonHyponym;
    private JButton jButtonAntonym;
    private JButton jButtonMeronym;
    private JButton jButtonSearchWordNet;
    private JPopupMenu jPopupMenu1;
    private JRadioButton jRadioButtonAdjective;
    private JRadioButton jRadioButtonAdverb;
    private JRadioButton jRadioButtonAll;
    private JRadioButton jRadioButtonNoun;
    private JRadioButton jRadioButtonVerb;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JTextField jTextFieldSearchItem;
    private JMenuItem mSearch;
    
    private JWNLBean mBean;    
}
