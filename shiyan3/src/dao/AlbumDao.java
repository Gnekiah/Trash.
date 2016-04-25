package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Album;

public class AlbumDao {
    
    public void Createalbum(Album album) throws ClassNotFoundException, SQLException{
        Connection connection = new JDBCConnection().getConnection();
        String sqlString = "create table "+ album.getName()+"(name varchar(20) not null,image varchar(50),description varchar(MAX),"
                + "primary key (name))";
        String sqlString2 = "insert into album values(?,?)";
        PreparedStatement ps = connection
                .prepareStatement(sqlString);
        ps.execute();
        PreparedStatement ps2 = connection
                .prepareStatement(sqlString2);
        ps2.setString(1, album.getName());
        ps2.setString(2, album.getDescription());
        ps2.execute();
        ps.close();
        ps2.close();
    }
    
    public List<Album> getAlbums() throws ClassNotFoundException, SQLException{
        List<Album> list = new ArrayList<Album>();
        Connection connection = new JDBCConnection().getConnection();
        PreparedStatement ps = connection
                .prepareStatement("select * from album");
        ps.execute();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            Album album = new Album();
            album.name = rs.getString("name");
            album.description = rs.getString("description");
            album.image = "image/tiger.jpg";
            list.add(album);
        }
        return list;
    }
    
    public void updateAlbum(String nameString,String description,String oldname) throws ClassNotFoundException, SQLException{
        Connection connection = new JDBCConnection().getConnection();
        PreparedStatement ps = connection
                .prepareStatement("update album set name=?,description=? where name=?");
        ps.setString(1, nameString);
        ps.setString(2, description);
        ps.setString(3, oldname);
        ps.execute();
        PreparedStatement ps2 = connection
                .prepareStatement("EXEC sp_rename ?,?");
        ps2.setString(1, oldname);
        ps2.setString(2, nameString);
        ps2.execute();
        ps.close();
        ps2.close();
        connection.close();
    }
    
    public void deleteAlbum(String nameString) throws ClassNotFoundException, SQLException{
        Connection connection = new JDBCConnection().getConnection();
        PreparedStatement ps = connection
                .prepareStatement("delete from album where name=?");
        ps.setString(1, nameString);
        ps.execute();
        String sqlString = "drop table "+nameString;
        PreparedStatement ps2 = connection
                .prepareStatement(sqlString);
        ps2.execute();
        ps.close();
        ps2.close();
        connection.close();
    }
}
