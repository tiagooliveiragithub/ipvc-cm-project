package online.tripguru.tripguruapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import online.tripguru.tripguruapp.models.Local

@Dao
interface LocalDao {
     @Query("SELECT * FROM local WHERE tripId = :tripId")
     fun getLocalsForTrip(tripId: Int): LiveData<List<Local>>
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(local: Local)

     @Query("SELECT * FROM local WHERE id = :localId")
     fun getLocalById(localId: Int): LiveData<Local>
}