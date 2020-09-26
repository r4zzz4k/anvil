package dev.inkremental.sample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import dev.inkremental.dsl.android.*
import dev.inkremental.dsl.android.Size.MATCH
import dev.inkremental.dsl.android.Size.WRAP
import dev.inkremental.dsl.android.widget.*
import dev.inkremental.dsl.androidx.core.CompatTextViewScope
import dev.inkremental.dsl.androidx.core.CompatViewScope
import dev.inkremental.r
import dev.inkremental.renderableContentView

class BasicsActivity : AppCompatActivity() {

    // Our state that we want to render using Anvil
    var ticktock = 0

    var tv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this

        // RenderableView wraps Anvil and hooks into View lifecycle
        renderableContentView {
            relativeLayout {
                size(MATCH, MATCH)

                linearLayout {
                    size(MATCH, MATCH)
                    padding(8.dp)
                    orientation(LinearLayout.VERTICAL)

                    textView {
                        init { tv = it }
                        size(MATCH, WRAP)
                        textSize(20f.sp)
                        text("Tick-tock: $ticktock")
                    }

                    linearLayout {
                        size(MATCH, WRAP)
                        orientation(LinearLayout.HORIZONTAL)

                        button {
                            size(WRAP, WRAP)
                            text("Tick")
                            // onClick conveniently calls Anvil.render()
                            // after executing listener lambda to render updated state
                            onClick {
                                ticktock++
                            }
                        }

                        button {
                            size(WRAP, 50.sizeDp)
                            margin(8.dp, 0.dp)

                            text("Tock")
                            // You can have more advanced logic here
                            onClick {
                                ticktock = (ticktock - 1).coerceAtLeast(0)

                                Toast.makeText(context, "Text: ${tv?.text}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    // You can use cycles!
                    for (i in 1..ticktock) {
                        textView {
                            size(MATCH, WRAP)
                            padding(4.dp)
                            text("$i")
                            // And conditionals too!
                            if (i % 2 == 1 && i % 5 == 0) {
                                primaryStyle()
                            } else if (i % 2 == 1) { //And do dynamic styling!
                                accentStyle()
                            } else {
                                standardStyle()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun TextViewScope.standardStyle() {
    backgroundColor(Color.TRANSPARENT)
}

fun accentStyle() {
    CompatTextViewScope.textColorCompat(R.color.white)
    CompatViewScope.backgroundColorCompat(R.color.colorAccent)
}

fun primaryStyle() {
    CompatTextViewScope.textColorCompat(R.color.white)
    CompatViewScope.backgroundColorCompat(R.color.colorPrimary)
}
