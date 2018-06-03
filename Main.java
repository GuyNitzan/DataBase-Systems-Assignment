import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	 protected static final String DB_NAME = "ASS3";

	 public static void main(String[] argv) throws SQLException, IOException {

	 System.out.println("-------- Oracle JDBC Connection Testing ------");

	 try {

	 Class.forName("oracle.jdbc.driver.OracleDriver");

	 } catch (ClassNotFoundException e) {

	 System.out.println("Where is your Oracle JDBC Driver?");
	 e.printStackTrace();
	 return;

	 }

	 System.out.println("Oracle JDBC Driver Registered!");

	 Connection connection = null;
	 Statement stmt = null;

	 try {

	 connection = DriverManager.getConnection(
	 "jdbc:oracle:thin:@132.72.40.216:1522:orcl", "user20",
	 "pass2021");

	 } catch (SQLException e) {

	 System.out.println("Connection Failed! Check output console");
	 e.printStackTrace();
	 return;

	 }

	 if (connection != null) {
	 System.out.println("You made it, take control your database now!");
	 stmt = connection.createStatement();
	 
	 DatabaseMetaData md = connection.getMetaData();
	 ResultSet rs;
	 ResultSetMetaData rsmd;
	 rs = md.getTables(null, null, "EDUCATION", null);
	 if (rs.next())
		 stmt.executeUpdate("DROP TABLE EDUCATION");
	 rs = md.getTables(null, null, "MARRIAGE", null);
	 if (rs.next())
		 stmt.executeUpdate("DROP TABLE MARRIAGE");
	 rs = md.getTables(null, null, "PERSONS", null);
	 if (rs.next())
		 stmt.executeUpdate("DROP TABLE PERSONS");
		 

		 //CREATING THE EDUCATION TABLE:
		 String EDUCATION_creation = "CREATE TABLE EDUCATION "  +                                   
	    			   "(PERSON_ID INTEGER , " +
	    			   "SCHOOL_NAME VARCHAR(255) , " +
	    			   "SCHOOL_COUNTRY VARCHAR(255) , " +
	    			   "DEGREE VARCHAR(255) , " +
	    			   "GRADUATION_YEAR INTEGER ," +
	    			   "PRIMARY KEY ( PERSON_ID , DEGREE ))";
		 stmt.executeUpdate(EDUCATION_creation);
		 
		//CREATING THE MARRIAGE TABLE:
		 String MARRIAGE_creation = "CREATE TABLE MARRIAGE "  +                                   
		    			   "(PERSON_ID INTEGER , " +
		    			   "RELATIVE_ID INTEGER ," +
		    			   "RELATIVE_TYPE VARCHAR(255) ," +
		    			   "PRIMARY KEY ( PERSON_ID , RELATIVE_ID ))";
		 stmt.executeUpdate(MARRIAGE_creation);
		 
		//CREATING THE PERSONS TABLE:
		 String PERSONS_creation = "CREATE TABLE PERSONS "  +      
				 		   "(PERSON_ID INTEGER , " +
		    			   "AGE INTEGER , " +
		    			   "WORK_CLASS VARCHAR(255) ," +
		    			   "FNLWGT INTEGER ," +
		    			   "EDUCATION VARCHAR(255) ," +
		    			   "EDUCATION_NUM INTEGER ," +
		    			   "MARITAL_STATUS VARCHAR(255) ," +
		    			   "OCCUPATION VARCHAR(255) ," +
		    			   "RELATIONSHIP VARCHAR(255) ," +
		    			   "RACE VARCHAR(255) ," +
		    			   "SEX VARCHAR(255) ," +
		    			   "CAPITAL_GAIN INTEGER ," +
		    			   "CAPITAL_LOSS INTEGER ," +
		    			   "WEEKLY_HOURS INTEGER ," +
		    			   "NATIVE_COUNTRY VARCHAR(255) ," +
		    			   "PRIMARY KEY ( PERSON_ID ))";
		 stmt.executeUpdate(PERSONS_creation);
		 System.out.println("Tables created successfully");
		 
		 // Integrities forced here
		 
		 /*String trigger =
     			"create or replace TRIGGER age_check  "+
     		    "BEFORE INSERT OR UPDATE OR DELETE  "+
     		    "ON MARRIAGE  "+
     		    "FOR EACH ROW "+
     		    
     		"DECLARE "+
     		    "age1 number;"+
     		    "age2 number;"+

     		"BEGIN "+

     		  "SELECT age "+
     		    "INTO age1 "+
     		    "FROM persons "+
     		   "WHERE PERSON_ID = :new.PERSON_ID;"+

     		  "SELECT age "+
     		    "INTO age2 "+
     		    "FROM persons "+
     		   "WHERE RELATIVE_ID = :new.RELATIVE_ID;"+

     		    "IF (age2>age1 and :new.RELATIVE_TYPE='child')"+
     		        "THEN  "+
     		        "RAISE_APPLICATION_ERROR (-20004,'A person age must be larger than his child');  "+
     		    "END IF;  "+
     		        
     		    "IF (:new.PERSON_ID=:new.RELATIVE_ID)"+
     	        "THEN  "+
     	        "RAISE_APPLICATION_ERROR (-20004,'A child in not a parent of him self');  "+
     	    "END IF;  "+
     		    
     		        "IF (age1<12 and :new.RELATIVE_TYPE='child')"+
     		        "THEN  "+
     		        "RAISE_APPLICATION_ERROR (-20004,'A parent must be at least 12 years old');  "+
     		    "END IF;  "+
     		    
     		"END;";*/
        	
		/* String trigger = 
        			"create or replace TRIGGER age_check  "+
        	     		    "BEFORE INSERT OR UPDATE OR DELETE  "+
        	     		    "ON MARRIAGE  "+
        	     		    "FOR EACH ROW "+
        	     		    
        	     		"DECLARE "+
        	     		    "age1 number;"+
        	     		    "age2 number;"+

        	     		"BEGIN "+

        	     		  "SELECT AGE "+
        	     		    "INTO age1 "+
        	     		    "FROM PERSONS "+
        	     		   "WHERE PERSON_ID = :new.PERSON_ID;"+

        	     		  "SELECT AGE "+
        	     		    "INTO age2 "+
        	     		    "FROM PERSONS "+
        	     		   "WHERE RELATIVE_ID = :new.RELATIVE_ID;"+

        	     		    "IF (age2>age1 and new.RELATIVE_TYPE='child')"+
        	     		        "THEN  "+
        	     		        "DELETE FROM MARRIAGE WHERE PERSON_ID = old.PERSON_ID AND RELATIVE_ID = old.RELATIVE_ID AND RELATIVE_TYPE = old.RELATIVE_TYPE;  "+ //RAISE_APPLICATION_ERROR (-20004,'A person age must be larger than his child')
        	     		    "END IF;  "+
        	     		        
        	     		    "IF (new.PERSON_ID=new.RELATIVE_ID)"+
        	     	        "THEN  "+
        	     	        "DELETE FROM MARRIAGE WHERE PERSON_ID = old.PERSON_ID AND RELATIVE_ID = old.RELATIVE_ID AND RELATIVE_TYPE = old.RELATIVE_TYPE;  "+ //RAISE_APPLICATION_ERROR (-20004,'A child in not a parent of him self'); 
        	     	    "END IF;  "+
        	     		    
        	     		        "IF (age1<12 and :new.RELATIVE_TYPE='child')"+
        	     		        "THEN  "+
        	     		        "DELETE FROM MARRIAGE WHERE PERSON_ID = old.PERSON_ID AND RELATIVE_ID = old.RELATIVE_ID AND RELATIVE_TYPE = old.RELATIVE_TYPE; "+ //RAISE_APPLICATION_ERROR (-20004,'A parent must be at least 12 years old');
        	     		    "END IF;  "+
        	     		    
        	     		"END;";*/
        	//stmt.executeUpdate(trigger) ;
		 //Finish forcing integrities
		 
		 boolean education_loaded= false, persons_loaded= false, marriage_loaded = false, integrities_loaded=false;
		 
		 PrintWriter writer = null;
		 try{
			    writer = new PrintWriter("the-file-name.txt", "UTF-8");
			} catch (IOException e) {
			   System.out.println("cannot creat an output file");
			}
		 while (true){
			 System.out.print("> ");
			 Scanner scan = new Scanner(System.in);
			 String input;
			 input = scan.nextLine();
			 String[] splitInput = input.split(" ");
			 switch (input.charAt(0)){
			 case 'l':
				 File file = new File(splitInput[1]);
	
			 	 try {
	
			        Scanner sc = new Scanner(file);
			        
			        if (splitInput[2].charAt(0) == 'E') {
				        	int id;
				        	String school;
				        	String state;
				        	String degree;
				        	int year;
					        while (sc.hasNextLine()) {
					        	school = "'";
					        	state = "'";
					        	degree = "'";
					            String next = sc.nextLine();
					            String[] splitNext = next.split(",");
					            id = Integer.parseInt(splitNext[0]);
					            year = Integer.parseInt(splitNext[4]);
					            school = school.concat(splitNext[1]);
					            school = school.concat("'");
					            state = state.concat(splitNext[2]);
					            state = state.concat("'");
					            degree = degree.concat(splitNext[3]);
					            degree = degree.concat("'");
					            String sql = "INSERT INTO EDUCATION " +
					                    "VALUES ("+id+","+ school+","+ state+","+ degree+","+ year + " )";
					            stmt.executeUpdate(sql);
					        }
					        education_loaded = true;
					        System.out.println("EDUCATION table loaded successfully");
			        }
			        else if (splitInput[2].charAt(0) == 'P'){
			        			   int id;
				    			   int age;
				    			   String work_class;
				    			   int fnlwgt;
				    			   String education;
				    			   int education_num;
				    			   String marital_status;
				    			   String occupation;
				    			   String relationship;
				    			   String race;
				    			   String sex;
				    			   int capital_gain;
				    			   int capital_loss;
				    			   int weekly_hours;
				    			   String native_country;
				    			   while (sc.hasNextLine()) {
				    				   work_class = "'";
				    				   education = "'";
				    				   marital_status = "'";
				    				   occupation = "'";
				    				   relationship = "'";
				    				   race = "'";
				    				   sex = "'";
				    				   native_country = "'";
							            String next = sc.nextLine();
							            String[] splitNext = next.split(",");
							            id = Integer.parseInt(splitNext[0]);
							            age = Integer.parseInt(splitNext[1]);
							            work_class = work_class.concat(splitNext[2]);
							            work_class = work_class.concat("'");
							            if (work_class.equals("'Private'") &&work_class.equals("'Self-emp-not-inc'") &&work_class.equals("'Self-emp-inc'") &&work_class.equals("'Federal-gov'")
							            		&&work_class.equals("'Local-gov'") 
							            		&& work_class.equals("'State-gov'") &&work_class.equals("'Without-pay'") &&work_class.equals("'Never-worked'") &&work_class!=null){
							            	continue;
							            }
							            fnlwgt = Integer.parseInt(splitNext[3]);
							            education = education.concat(splitNext[4]);
							            education = education.concat("'");
							            education_num = Integer.parseInt(splitNext[5]);
							            marital_status = marital_status.concat(splitNext[6]);
							            marital_status = marital_status.concat("'");
							            occupation = occupation.concat(splitNext[7]);
							            occupation = occupation.concat("'");
							            relationship = relationship.concat(splitNext[8]);
							            relationship = relationship.concat("'");
							            race = race.concat(splitNext[9]);
							            race = race.concat("'");
							            sex = sex.concat(splitNext[10]);
							            sex = sex.concat("'");
							            capital_gain = Integer.parseInt(splitNext[11]);
							            capital_loss = Integer.parseInt(splitNext[12]);
							            weekly_hours = Integer.parseInt(splitNext[13]);
							            native_country = native_country.concat(splitNext[14]);
							            native_country = native_country.concat("'");
	
							            String sql = "INSERT INTO PERSONS " +
							                    "VALUES ("+id+","+ age+","+work_class+","+ fnlwgt+","+ education +","+ education_num +","+ marital_status +","+ occupation +
							                    ","+ relationship +","+ race +","+ sex +","+ capital_gain +","+ capital_loss +","+ weekly_hours +","+ native_country +" )";
							            stmt.executeUpdate(sql);
							        }
				    			   persons_loaded = true;
				    			   System.out.println("PERSONS table loaded successfully");
			        	
			        }
			        else if (splitInput[2].charAt(0) == 'M'){
			        	int id;
			        	int relative_id;
			        	String relation;
			        	while (sc.hasNextLine()) {
			        		relation = "'";
			        		String next = sc.nextLine();
				            String[] splitNext = next.split(",");
				            id = Integer.parseInt(splitNext[0]);
				            relative_id = Integer.parseInt(splitNext[1]);
				            relation = relation.concat(splitNext[2]);
				            relation = relation.concat("'");
				            String sql = "INSERT INTO MARRIAGE " +
				                    "VALUES ("+id+","+ relative_id+","+ relation+" )";
				            stmt.executeUpdate(sql);
			        	}
			        	marriage_loaded = true;
			        	System.out.println("MARRIAGE table loaded successfully");
			        }
			        sc.close();
			 	 } 
			 	 catch (FileNotFoundException e) {
				        e.printStackTrace();
				 }
			 	 break;
			     case 'p':
				 
					String table = splitInput[1];
					String query = "SELECT * FROM "+table;
					rs = stmt.executeQuery(query);
					ResultSetMetaData rsmdata = rs.getMetaData();
					int numOfColumns = rsmdata.getColumnCount();
					// print Column Names
					for (int i = 1; i <= numOfColumns; i++)
						System.out.print(rsmdata.getColumnName(i) + "\t");
					System.out.println();
					// print data
					while (rs.next()) {        
						for(int i = 1 ; i <= numOfColumns; i++)
							System.out.print(rs.getString(i) + "\t"); //Print one element of a row
						System.out.println();		//Move to the next line to print the next row.           
					}
				  
				  
				 
				 
				 break;
			     case 's':
			    	 int l = input.length();
			    	 ResultSet rs1 = stmt.executeQuery(input.substring(4, l));
			    	 rs1.next();
			    	 System.out.println(rs1.getInt(1));
			    	 
			     break;
			     
//load education.txt EDUCATION
//load persons.txt PERSONS
//load marriage.txt MARRIAGE		
			     
			     case 'r': //report
			    	 int op = Integer.parseInt(splitInput[1]);
					 switch (op) {
					 case 1: 
						rs = stmt.executeQuery("SELECT PERSON_ID FROM PERSONS "+
												"ORDER BY CAPITAL_GAIN-CAPITAL_LOSS DESC");
						rs.next();
						System.out.println("richest person id is: "+rs.getInt("PERSON_ID"));
						writer.println("richest person id is: "+rs.getInt("PERSON_ID"));
						break;
					 case 2: 
						String country = splitInput[2];
						rs = stmt.executeQuery("SELECT PERSONS.PERSON_ID , COUNT(*) "+
												"FROM PERSONS JOIN MARRIAGE "+
													"ON PERSONS.PERSON_ID = MARRIAGE.PERSON_ID "+
												"WHERE NATIVE_COUNTRY = '"+ country + "' AND MARRIAGE.RELATIVE_TYPE = 'child' " +
												"GROUP BY PERSONS.PERSON_ID " +
												"ORDER BY COUNT(*) ASC ");
						if(rs.next())
							System.out.println(rs.getInt(1)+" "+rs.getInt(2));
						break;
					 case 3: 
						 /*rs = stmt.executeQuery("SELECT NATIVE_COUNTRY , AVG(aaa)" +
								 					"FROM "+
								 						"(SELECT NATIVE_COUNTRY , PERSON_ID , COUNT(*) AS aaa INTO #a "+
															 "FROM PERSONS JOIN MARRIAGE "+
															 	"ON PERSONS.PERSON_ID = MARRIAGE.PERSON_ID "+
															 "WHERE PERSONS.SEX = 'Female' AND MARRIAGE.RELATIVE_TYPE = 'child' "+
															 "GROUP BY PERSON_ID) "+
													"GROUP BY NATIVE_COUNTRY");*/
						 
						 rs = stmt.executeQuery("SELECT NATIVE_COUNTRY , PERSON_ID , COUNT(*) AS cnt INTO tempp "+
													 "FROM PERSONS JOIN MARRIAGE "+
													 	"ON PERSONS.PERSON_ID = MARRIAGE.PERSON_ID "+
													 "WHERE PERSONS.SEX = 'Female' AND MARRIAGE.RELATIVE_TYPE = 'child' "+
													 "GROUP BY PERSON_ID ");
								 
						 rs = stmt.executeQuery("SELECT a.NATIVE_COUNTRY , AVG(cnt) " +
								 					"FROM tempp a "+
													"GROUP BY a.NATIVE_COUNTRY ");
						 
						rsmd = rs.getMetaData();
						int ansNumOfColumns = rsmd.getColumnCount();
						System.out.println("COUNTRY\t\tAVG OF WOMEN KIDS");
						while (rs.next()) {        
							for(int i = 1 ; i <= ansNumOfColumns; i++) {
								System.out.print(rs.getString(i) + "\t\t"); //Print one element of a row
								writer.print(rs.getString(i) + "\t\t");
							}
							System.out.println();		//Move to the next line to print the next row.           
							writer.println();
						}	
						break;
					 case 4: // BONUS
						System.out.println("Bonus - maybe next time 🙂");
						break;
					 default:
						System.out.println("bad #ofReport");
						break;
					}
			    	 
			    	 
			     break;
			     case 'q':
					 writer.close();
					 return;
				 default: 
					 System.out.println("command unknown");
					 break;
		        
		     }
			 
			/* if (education_loaded && persons_loaded && marriage_loaded && !integrities_loaded){
				 forceIntegrities(stmt);
				 integrities_loaded = true;
				 System.out.println("integrities loaded");
			 }*/
		 } 
	 
	 } else {
	 System.out.println("Failed to make connection!");
	 }
	 }
	 

	public static void forceIntegrities(Statement stmt) throws SQLException{
		forceParentOlderThanChild(stmt);
		forceParentOlderThan12(stmt);
		forceWifeMustEarnAtLeastAsMan(stmt);
	}
	
	/*public static void forceParentOlderThanChild(Statement stmt) throws SQLException {
        ResultSet ages;
        ages = stmt.executeQuery("SELECT PERSONS.PERSON_ID , AGE1 , AGE2 FROM PERSONS JOIN MARRIAGE "+
								       	"ON PERSONS.PERSON_ID = MARRIAGE.PERSON_ID " +
								       	"WHERE RELATIVE_TYPE = 'child' AND " +
								       	"AGE1 =  PERSONS.AGE1 " +
								       	"" );
								/*        " +
								       	  "PERSONS.AGE <= (SELECT PERSONS.AGE FROM PERSONS JOIN MARRIAGE " +
									       				"ON PERSONS.PERSON_ID = MARRIAGE.RELATIVE_ID " +
									       				"WHERE RELATIVE_TYPE = 'child' " +
									       				"ORDER BY MARRIAGE.PERSON_ID DESC) ");
        while (ages.next()) {
        	ages.deleteRow();
        }
    }*/
	public static void forceParentOlderThanChild(Statement stmt) throws SQLException{
		ResultSet allParents, parent, child;
		ArrayList<ParentChildPair> L = new ArrayList<ParentChildPair>();
		int parent_id, child_id;
		allParents = stmt.executeQuery("SELECT PERSON_ID,RELATIVE_ID FROM MARRIAGE WHERE MARRIAGE.RELATIVE_TYPE = 'child'");
		while (allParents.next()){
			parent_id = allParents.getInt("PERSON_ID");
			//parent = stmt.executeQuery("SELECT AGE FROM PERSONS WHERE PERSON_ID="+parent_id );
			child_id = allParents.getInt("RELATIVE_ID");
			//child = stmt.executeQuery("SELECT AGE FROM PERSONS WHERE PERSON_ID="+child_id);
			//L.add(new ParentChildPair(parent,child));
			//deleteRelation(stmt,parent_id,child_id);
		}
		for(int i=0; i<L.size(); i++){
			L.get(i).getChild().next();
			int c = L.get(i).getChild().getInt(1);
			L.get(i).getParent().next();
			int p = L.get(i).getParent().getInt(1);
			if (p<=c){
				stmt.executeQuery("DELETE FROM MARRIAGE WHERE PARENT_ID = "+p+"AND RELATIVE_ID = "+c);
			}
		}
	}
	
	public static void forceWifeMustEarnAtLeastAsMan(Statement stmt) throws SQLException {		// ALL WOMEN THAT EARN LESS THAN HUSBAND
		ResultSet rs;
		rs = stmt.executeQuery("SELECT PERSONS.PERSON_ID , CAPITAL_GAIN , CAPITAL_LOSS FROM PERSONS JOIN MARRIAGE "+
							       	"ON PERSONS.PERSON_ID = MARRIAGE.PERSON_ID " +
							       	"WHERE RELATIVE_TYPE = 'husband' AND CAPITAL_GAIN-CAPITAL_LOSS < " +
							       		"(SELECT CAPITAL_GAIN-CAPITAL_LOSS FROM PERSONS JOIN MARRIAGE " +
							       				"ON PERSONS.PERSON_ID = MARRIAGE.RELATIVE_ID " +
							       				"WHERE RELATIVE_TYPE = 'husband' ) ");// AND EARNINGS = CAPITAL_GAIN-CAPITAL_LOSS) ");
		while (rs.next()) {
			rs.deleteRow();
	    }
	}
	
	public static void forceParentOlderThan12(Statement stmt) throws SQLException{
		int age;
		ResultSet allParents = stmt.executeQuery("SELECT PERSONS.PERSON_ID , PERSONS.AGE " +
													"FROM PERSONS JOIN MARRIAGE ON PERSONS.PERSON_ID = MARRIAGE.PERSON_ID " +
													"WHERE RELATIVE_TYPE = 'child' ");
		while (allParents.next()) {
			age = allParents.getInt("PERSONS.AGE");
			if (age <= 12) allParents.deleteRow();
		}
	}
	public static int childCount(Statement stmt, int id) throws SQLException{
		ResultSet children = stmt.executeQuery("SELECT COUNT(*) FROM MARRIAGE WHERE PERSON_ID="+id+" AND RELATIVE_TYPE = 'child'");
		if (children.next())
			return children.getInt(1);
		else return 0;
	}
	
	/*public static void deleteRelation(Statement stmt, int parent, int child){
		ResultSet parentRS, childRS;
		parentRS = stmt.executeQuery();
	}*/
}
	

//load education.txt EDUCATION
//load persons.txt PERSONS
//load marriage.txt MARRIAGE
//sql select PERSON_ID from MARRIAGE where PERSON_ID=195 and RELATIVE_ID=87

