package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultDAO {

	public double executeQuery(String query){
		try{
			PreparedStatement pstmt;
			try(Connection conn = DBConnector.getConnection()){
				pstmt = conn.prepareStatement(query);

//				long start = System.currentTimeMillis();
	            ResultSet rs = pstmt.executeQuery();
//	            long end = System.currentTimeMillis();
//				System.out.println(1.0*(end - start)/1000);
//				System.out.println();
	            while(rs.next()){
					
				}
			}
			pstmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
//		System.out.println(town.size());
		return 0;
	}
	
}
