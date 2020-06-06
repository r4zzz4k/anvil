package dev.inkremental.sample

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import dev.inkremental.android.*
import dev.inkremental.dsl.android.*
import dev.inkremental.dsl.android.widget.LinearLayoutScope
import dev.inkremental.dsl.android.widget.TextViewScope

class XmlActivity : AppCompatActivity() {

    var isSpinning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        renderableContentView {
            xml(R.layout.activity_xml_layout, LinearLayoutScope) {
                orientation(LinearLayout.HORIZONTAL)
                margin(5.dp)

                withId(R.id.progress_btn, TextViewScope) {
                    text(if (isSpinning) "Stop progress" else "Start progress")
                    // button click listener can be attached
                    onClick { v ->
                        isSpinning = !isSpinning
                    }
                }

                withId(R.id.progress) {
                    visibility(isSpinning)
                }

            }
        }
    }
}
