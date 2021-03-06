package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Trait
import io.objectbox.BoxStore
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor
import io.objectbox.relation.ToMany

@Entity
class TraitDao() {
    @Id(assignable = true) var id: Long = 0
    var name: String = ""
    var description: String = ""
    var meta: Boolean = false
    var chars: Int = 0
    lateinit var aliases: ToMany<TraitAlias>
    lateinit var parents: ToMany<TraitParent>

    constructor(trait: Trait, boxStore: BoxStore) : this() {
        id = trait.id
        name = trait.name
        description = trait.description
        meta = trait.meta
        chars = trait.chars

        boxStore.boxFor<TraitDao>().attach(this)
        trait.aliases.forEach { aliases.add(TraitAlias(it.hashCode().toLong(), it)) }
        trait.parents.forEach { parents.add(TraitParent(it)) }

        boxStore.boxFor<TraitAlias>().put(aliases)
        boxStore.boxFor<TraitParent>().put(parents)
    }

    fun toBo() = Trait(
        id,
        name,
        description,
        meta,
        chars,
        aliases.map { it.alias },
        parents.map { it.id }
    )
}

@Entity
data class TraitAlias(
    @Id(assignable = true) var id: Long = 0,
    var alias: String = ""
)

@Entity
data class TraitParent(
    @Id(assignable = true) var id: Long = 0
)