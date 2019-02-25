package com.example.testpalettelibrary


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.example.testpalettelibrary.adapter.PaletteAdapter
import com.example.testpalettelibrary.adapter.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_palette.*


class PaletteFragment : Fragment(), PaletteAdapter.RecyclerViewOnClickItem {

    private val paletteAdapter = PaletteAdapter(this)

    private var colors = arrayListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_palette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        //val colors = resources.getIntArray(R.array.color_array)

        recyclerView.apply {
            addItemDecoration(SpacesItemDecoration(requireContext().resources.getDimensionPixelSize(R.dimen.circle_space)))
            adapter = paletteAdapter
        }

        btnGetColor.setOnClickListener {
            createPaletteAsync()
        }
    }

    private fun createPaletteAsync() {
        if (imgPreview.isLaidOut) {
            if (colors.isEmpty()) {
                try {
                    val bitmap = imgPreview.drawToBitmap()
                    Palette.Builder(bitmap)
                        .maximumColorCount(50)
                        .generate { palette ->
                            palette?.run {
                                swatches.forEach {
                                    colors.add(it.rgb)
                                    //colors.add(ColorUtils.HSLToColor(it.hsl))
                                }
                            }
                            // colors.add(0, Color.WHITE)
                            // colors.add(1, Color.BLACK)
                            // colors.sortByDescending { it }
                            paletteAdapter.addData(colors)
                        }
                } catch (e: Exception) {
                    Log.e("TAG", "Error parse color!")
                }
            }
        }
    }

    override fun onClick(color: Int) {
        btnGetColor.apply {
            setBackgroundColor(color)
            setTextColor(if (isDark(color)) Color.WHITE else Color.BLACK)
        }
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }

    //fun createPaletteSync(bitmap: Bitmap) = Palette.from(bitmap).generate()
}
