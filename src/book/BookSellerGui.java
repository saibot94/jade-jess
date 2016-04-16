package book;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookSellerGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BookSellerAgent myAgent;

	public BookSellerGui(BookSellerAgent bookSellerAgent) {
		super(bookSellerAgent.getLocalName());

		
		this.myAgent = bookSellerAgent;

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(new JLabel("Book title: "));
		final JTextField titleField = new JTextField(15);
		panel.add(titleField);

		panel.add(new JLabel("Book price: "));
		final JTextField priceField = new JTextField(15);
		panel.add(priceField);

		getContentPane().add(panel, BorderLayout.NORTH);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {

					String title = titleField.getText().trim();
					String price = priceField.getText().trim();
					myAgent.updateCatalogue(title, Integer.parseInt(price));
					System.out.println("Added book: " + title + " Price= " + price);
					
					titleField.setText("");
					priceField.setText("");
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getMessage());
				}
			}
		});

		JPanel panel2 = new JPanel();
		panel2.add(addButton);
		getContentPane().add(panel2, BorderLayout.SOUTH);
		

		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		} );
		
		setSize(450, 200);
		setResizable(false);
	}

}
