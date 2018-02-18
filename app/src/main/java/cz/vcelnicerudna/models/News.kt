package cz.vcelnicerudna.models

import java.util.*

class News {
    var id: Int = 0
    lateinit var title: String
    lateinit var text: String
    lateinit var icon: String
    lateinit var created: Date
    lateinit var updated: Date
}
