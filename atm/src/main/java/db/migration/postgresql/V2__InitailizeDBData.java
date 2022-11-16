package db.migration.postgresql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class V2__InitailizeDBData extends BaseJavaMigration
{
	private static final String COMMA_DELIMITER = ",";
	
	
	@Override
	public void migrate(Context context) throws Exception 
	{
		log.info("Inserting initial data.");
		final Resource res = new ClassPathResource("data/atms.csv");
		
		/*
		 * Simple CSV reading to remove the need of pulling in external dependencies.
		 */
		try (BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream()))) 
		{				        
            final var insert = "insert into atm (name, addr, city, state, postalCode, inDoors, latitude, longitude, cord, branchId) values (?, ?, ?, ?, ?, ?, ?, ?, ST_PointFromText(?, 4326), ?)";
			
			try(PreparedStatement statement = context.getConnection().prepareStatement(insert))
			{
			
			    String line;
			    while ((line = br.readLine()) != null) 
			    {
					final var values = line.split(COMMA_DELIMITER);
	
					{
						final var wkt = String.format("POINT(%f %f)", Float.parseFloat(values[6]), Float.parseFloat(values[7]));						
						
						statement.setString(1, values[0].strip());
						statement.setString(2, values[1].strip());
						statement.setString(3, values[2].strip());
						statement.setString(4, values[3].strip());
						statement.setString(5, values[4].strip());
						statement.setBoolean(6, values[5].strip().equals("TRUE"));
						statement.setFloat(7, Float.parseFloat(values[6]));
						statement.setFloat(8, Float.parseFloat(values[7]));
						statement.setString(9, wkt);
						statement.setObject(10, null);
						statement.addBatch();					
					}
			    }
			    
				statement.executeBatch();
			}
		}
	}

}
