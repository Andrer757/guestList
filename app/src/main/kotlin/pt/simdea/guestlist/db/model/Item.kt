package pt.simdea.guestlist.db.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Item(
        @Id var id: Long = 0,
        var item: String? = "",
        var counter: Long = 0
)