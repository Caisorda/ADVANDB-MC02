package view;

import java.awt.EventQueue;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OlapView window = new OlapView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
