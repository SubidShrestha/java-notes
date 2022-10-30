package notePad;

import javax.swing.*;
import java.awt.print.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.datatransfer.*;
import javax.swing.text.*;

public class NotePad {

	private JFrame frmNotes;
	private JPanel panel;
	private JTextPane textPane;
	public String FileName = "Untitled";
	public Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotePad window = new NotePad();
					window.frmNotes.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public NotePad() {
		initialize();
	}

	private void initialize() {
		frmNotes = new JFrame();
		frmNotes.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\DELL\\eclipse-workspace\\Notes\\src\\notePad\\notesicon.png"));
		frmNotes.setTitle("Notes");
		frmNotes.setResizable(false);
		frmNotes.setBounds(0, 0, 1321, 716);
		frmNotes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNotes.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(95, 158, 160));
		menuBar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		menuBar.setBounds(0, 0, 1321, 27);
		frmNotes.getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				FileName = "Untitled";
				textPane.setText(null);
				frmNotes.setTitle(FileName);
				
			}
		});
		mntmNew.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmNew);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				FileDialog fileDialog = new FileDialog(frmNotes,"Save File",FileDialog.SAVE);
				fileDialog.setVisible(true);
				
				if(fileDialog.getFile()!=null) {
					
					FileName = fileDialog.getDirectory() + fileDialog.getFile();
					frmNotes.setTitle(FileName);
					
				}
				
				try {
					
					FileWriter fw = new FileWriter(FileName);
					fw.write(textPane.getText());
					frmNotes.setTitle(FileName);
					fw.close();
					
					
				}catch(Exception ex) {
					
					ex.printStackTrace();
					
				}
				
			}
		});
		mntmSave.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmSave);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				FileDialog fileDialog = new FileDialog(frmNotes,"Open File",FileDialog.LOAD);
				fileDialog.setVisible(true);
				
				if(fileDialog.getFile()!=null) {
					
					FileName = fileDialog.getDirectory() + fileDialog.getFile();
					frmNotes.setTitle(FileName);
					
				}
				
				try {
					
					BufferedReader reader = new BufferedReader(new FileReader(FileName));
					StringBuilder stb = new StringBuilder();
					String Line = null;
					
					while((Line = reader.readLine())!=null) {
						
						stb.append(Line + "\n");
						textPane.setText(stb.toString());
						
					}
					
					reader.close();
					
				}catch(Exception ex) {
					
					ex.printStackTrace();
					
				}
				
			}
		});
		mntmOpen.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmOpen);
		
		class highlighter extends DefaultHighlighter.DefaultHighlightPainter{

			public highlighter(Color c) {
				super(c);
			}
			
		}
		
		Highlighter.HighlightPainter highlightedWord = new highlighter(Color.ORANGE);
		
		JMenuItem mntmSearch = new JMenuItem("Search");
		mntmSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JDialog searchDialog = new JDialog(frmNotes,"Search");
				searchDialog.getContentPane().setLayout(null);
				searchDialog.setVisible(true);
				searchDialog.setResizable(false);
				searchDialog.setBounds(200, 250, 350, 120);
				
				JLabel label = new JLabel("Type to search");
				label.setBounds(10, 10, 150, 20);
				
				JTextField searchBox = new JTextField(26);
				searchBox.setBounds(20, 40, 150, 20);
				
				JButton searchBtn = new JButton("Search");
				searchBtn.setBounds(190, 40, 100, 20);
				searchBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent ev) {
						
						search();
						
					}
					

					private void search(){
						
						Highlighter hilites = textPane.getHighlighter();
						Highlighter.Highlight[] remove = hilites.getHighlights();
						
						 for (int i = 0; i < remove.length; i++) {
						   
							 if (remove[i].getPainter() instanceof highlighter) {
						      
						    	  hilites.removeHighlight(remove[i]);
						      
						      }
						 }
							 
						try {
							      
							Highlighter hilite = textPane.getHighlighter();
							Document doc = textPane.getDocument();
							String item = doc.getText(0,doc.getLength());
							String word = searchBox.getText();
							
							int pos = 0;
							
							while((pos = item.toUpperCase().indexOf(word.toUpperCase(),pos))>=0) {
								hilite.addHighlight(pos,pos+word.length(), highlightedWord);
								pos += word.length();
								
							}
							
							
						}catch(Exception ex) {
							
							ex.printStackTrace();
							
						}
				
					}
				});
				
				searchDialog.getContentPane().add(searchBox);
				searchDialog.getContentPane().add(label);
				searchDialog.getContentPane().add(searchBtn);
				
			}
		});
		mntmSearch.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmSearch);
		
		JMenuItem mntmFind = new JMenuItem("Find and Replace");
		mntmFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JDialog findDialog = new JDialog(frmNotes,"Find and Replace");
				findDialog.getContentPane().setLayout(null);
				findDialog.setVisible(true);
				findDialog.setResizable(false);
				findDialog.setBounds(200, 250, 350, 240);
				
				JLabel label1 = new JLabel("Find:");
				label1.setBounds(5, 10, 150, 20);
				JLabel label2 = new JLabel("Replace:");
				label2.setBounds(5, 50, 150, 20);
				
				JTextField searchBox = new JTextField(26);
				searchBox.setBounds(40, 10, 150, 20);
				JTextField replaceBox = new JTextField(26);
				replaceBox.setBounds(60, 50, 130, 20);
				
				JButton searchBtn = new JButton("Find");
				searchBtn.setBounds(200, 10, 100, 20);
				searchBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent ev) {
						
						search();
						
					}
					

					private void search(){
						
						Highlighter hilites = textPane.getHighlighter();
						Highlighter.Highlight[] remove = hilites.getHighlights();
						
						 for (int i = 0; i < remove.length; i++) {
						   
							 if (remove[i].getPainter() instanceof highlighter) {
						      
						    	  hilites.removeHighlight(remove[i]);
						      
						      }
						 }
							 
						try {
							      
							Highlighter hilite = textPane.getHighlighter();
							Document doc = textPane.getDocument();
							String item = doc.getText(0,doc.getLength());
							String word = searchBox.getText();
							
							int pos = 0;
							
							while((pos = item.toUpperCase().indexOf(word.toUpperCase(),pos))>=0) {
								hilite.addHighlight(pos,pos+word.length(), highlightedWord);
								pos += word.length();
								
							}
							
							
						}catch(Exception ex) {
							
							ex.printStackTrace();
							
						}
				
					}
				});
				
				JButton replace = new JButton("Replace");
				replace.setBounds(200,50,100,20);
				replace.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						String a = searchBox.getText();
						String b = replaceBox.getText();
						String c = textPane.getText();
						
						String d = c.replaceAll(a, b);
						
						textPane.setText(d);
						
						
					}
					
				});
				
				findDialog.getContentPane().add(searchBox);
				findDialog.getContentPane().add(replaceBox);
				findDialog.getContentPane().add(label1);
				findDialog.getContentPane().add(label2);
				findDialog.getContentPane().add(searchBtn);
				findDialog.getContentPane().add(replace);
				
			}
				
		});
		mntmFind.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmFind);
		
		JMenuItem mntmPrint = new JMenuItem("Print");
		mntmPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					textPane.print();
					
				}catch(PrinterException ex) {
					
					System.err.format("Printer Not Found", ex.getMessage());
					
				}
				
			}
		});
		mntmPrint.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmPrint);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frmNotes = new JFrame();
				if(JOptionPane.showConfirmDialog(frmNotes, "Do you wish to exit Notes?","Exit"
						,JOptionPane.YES_NO_OPTION)==JOptionPane.YES_NO_OPTION) {
					
					System.exit(0);
					
				}
				
			}
		});
		mntmExit.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnEdit);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TransferHandler transferHandler = textPane.getTransferHandler();
				transferHandler.exportToClipboard(textPane, clipBoard, TransferHandler.COPY);
				textPane.replaceSelection(null);
				
			}
		});
		mntmCut.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnEdit.add(mntmCut);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TransferHandler transferHandler = textPane.getTransferHandler();
				transferHandler.exportToClipboard(textPane, clipBoard, TransferHandler.COPY);
				
			}
		});
		mntmCopy.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TransferHandler transferHandler = textPane.getTransferHandler();
				transferHandler.importData(textPane, clipBoard.getContents(null));
				
			}
		});
		mntmPaste.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnEdit.add(mntmPaste);
		
		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textPane.selectAll();
				
			}
		});
		mntmSelectAll.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mnEdit.add(mntmSelectAll);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnHelp);
		JMenuItem mntmAbout = new JMenuItem("About Us");
		mntmAbout.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		mntmAbout.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				frmNotes = new JFrame("About us");
				frmNotes.setBounds(200, 200, 420, 400);
				frmNotes.setVisible(true);
				frmNotes.setResizable(false);
				frmNotes.setLayout(null);
				
				JPanel panel = new JPanel();
				panel.setLayout(null);
				panel.setBounds(0, 0, 420, 400);
				
				JLabel label = new JLabel("Made by Subid Shrestha a.k.a Fruits Punch Samurai G");
				label.setBounds(20, 10, 400, 70);
				
				panel.add(label);
				frmNotes.add(panel);
				
			}
			
		});
		mnHelp.add(mntmAbout);
		
				
		panel = new JPanel();
		panel.setBounds(0, 25, 1321, 663);
		frmNotes.getContentPane().add(panel);
		panel.setLayout(null);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		textPane.setBounds(0, 0, 1321, 663);
		panel.add(textPane);
		
	}
}
