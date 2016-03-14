package Server;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

public class InitialDB{
	public static void main(String[] args){
		//Initial the database
		try {
			DictionaryDB.Initial();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}