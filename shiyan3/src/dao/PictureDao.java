package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.picture;

public class PictureDao {
    
    public List<picture> getPictures(String name) throws ClassNotFoundException, SQLException{
        List<picture> listPictures = new ArrayList<picture>();
        Connection connection = new JDBCConnection().getConnection();
        String sqlString = "select * from "+name;
        PreparedStatement ps = connection
                .prepareStatement(sqlString);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        
        while (rs.next()) {
            picture pic = new picture();
            pic.name = rs.getString("name");
            pic.description = rs.getString("description");
            pic.image = "image/"+name+"/"+rs.getString("image");    
            System.out.print(pic.image+"\n");
            listPictures.add(pic);
        }
        return listPictures;
    }
    
    public void addpicture(picture pic,String album) throws ClassNotFoundException, SQLException{
        Connection connection = new JDBCConnection().getConnection();
        String sqlString = "insert into "+album+" values(?,?,?);";
        PreparedStatement ps = connection
                .prepareStatement(sqlString);
        ps.setString(1, pic.name);
        ps.setString(2, pic.image);
        ps.setString(3, pic.description);
        ps.execute();
        ps.close();
        connection.close();
    }
    
    public void deletepic(String albumname,String photoname) throws ClassNotFoundException, SQLException{
        Connection connection = new JDBCConnection().getConnection();
        String sqlString = "delete from "+albumname+" where name=?";
        PreparedStatement ps = connection
                .prepareStatement(sqlString);
        ps.setString(1, photoname);
        ps.execute();
        ps.close();
        connection.close();
    }
    
    public void picedit(String albumname,String photoname,String description,String oldname) throws ClassNotFoundException, SQLException{
        Connection connection = new JDBCConnection().getConnection();
        String sqlString = "update "+albumname+" set name=?,description=? where name=?";
        PreparedStatement ps = connection
                .prepareStatement(sqlString);
        ps.setString(1, photoname);
        ps.setString(2, description);
        ps.setString(3, oldname);
        ps.execute();
        ps.close();
        connection.close();
    }
}
