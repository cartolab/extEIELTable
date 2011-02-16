package es.udc.cartolab.gvsig.eielforms;
import java.io.File;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class ParserMain {

	/**
	 * @param args
	 */
	/*public static void main(String argv[]) {

		parseXmlFile("/home/jlopez/Escritorio/test2.xml");
		/*if (DBSession.getCurrentSession() == null) {
			try {
				DBSession.createConnection("193.144.59.201", 5432, "eiel_pontevedra_2009", "eiel_aplicaciones", "psanxiao", "i08carto");
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

	/*Connection connection = DBAux.getConnection();

		try {
			PreparedStatement query = connection.prepareStatement("select count(*) from \"eiel_aplicaciones\".dominios;");
			ResultSet resultSet = query.executeQuery();

			if (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}*/

	public static void parseXmlFile(String path) {
		try {
			//Create and set up the window.
			JFrame frame = new JFrame("SimpleTableDemo");
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			File file = new File(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			frame.setContentPane(Parser.parseXmlDoc(doc, null));

			//Display the window.
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
