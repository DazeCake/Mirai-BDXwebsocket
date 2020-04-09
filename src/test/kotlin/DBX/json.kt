package DBX

import com.github.dazecake.data.Incoming
import com.github.dazecake.util.BDXJson
import kotlinx.serialization.UnstableDefault

@OptIn(UnstableDefault::class)
fun main() {
    val raw = """
        {"operate":"onmsg","target":"WangYneos","text":"HelloWorld"}
    """.trimIndent()

    val parse = BDXJson.json.parse(Incoming.serializer(), raw)
    println(parse)
}