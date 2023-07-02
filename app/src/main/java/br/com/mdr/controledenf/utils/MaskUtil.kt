package br.com.mdr.controledenf.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class MaskUtil{
    enum class MaskType(val maskType: String) {
        CPF_CNPJ("CPF_CNPJ"),
        CPF("CPF"),
        CNPJ("CNPJ"),
        PHONE("PHONE"),
        DATE_BR("DATE_BR"),
        DATE("DATE"),
        CEP("CEP"),
        BR_CURRENCY("BR_CURRENCY")
    }
    companion object {
        const val maskCNPJ = "##.###.###/####-##"
        const val maskCPF = "###.###.###-##"
        const val maskPhone = "(##) ####-####"
        const val maskCell = "(##) #####-####"
        const val maskCep = "#####-###"
        const val maskDateBr = "##/##/####"
        const val maskDate = "####/##/##"
        const val maskBrCurrency = "##,##"

        fun removeMask(text : String) : String {
            return text.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }

        fun removeBrCurrencyMask(text : String) : String {
            return text
                .replace(".", "")
                .replace("R", "").replace(" ", "")
                .replace("$", "").replace(",", ".").trim()
        }

        fun insertMask(editText: TextInputEditText, maskType: MaskType): TextWatcher {
            val textWatcher = object : TextWatcher {
                var isUpdating = false
                var old = ""

                override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) = Unit
                override fun afterTextChanged(p0: Editable?) = Unit
                override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                    val str = removeMask(charSequence.toString())
                    var mask = ""
                    val defaultMask = getDefaultMask(str)

                    when (maskType) {
                        MaskType.CPF_CNPJ -> {
                            mask = when (str.length) {
                                11 -> {
                                    maskCPF
                                }

                                12, 13, 14 -> {
                                    maskCNPJ
                                }

                                else -> {
                                    defaultMask
                                }
                            }
                        }
                        MaskType.CPF -> {mask = maskCPF}
                        MaskType.CNPJ -> {mask = maskCNPJ}
                        MaskType.PHONE -> {
                            mask = when (str.length) {
                                10 -> {
                                    maskPhone
                                }

                                11 -> {
                                    maskCell
                                }

                                else -> {
                                    maskPhone
                                }
                            }
                        }
                        MaskType.DATE_BR -> {mask = maskDateBr}
                        MaskType.DATE -> {mask = maskDate}
                        MaskType.CEP -> {mask = maskCep}
                        MaskType.BR_CURRENCY -> {mask = maskBrCurrency}
                    }
                    var mascara = ""
                    if (isUpdating) {
                        old = str
                        isUpdating = false
                        return
                    }

                    var index = 0
                    for (m : Char in mask.toCharArray()){
                        if ((m != '#' && str.length > old.length) || (m != '#' && str.length < old.length)
                            && str.length != index) {
                            mascara += m
                            continue
                        }
                        try {
                            mascara += str[index]
                        }catch (e : Exception){
                            break
                        }
                        index++
                    }

                    isUpdating = true
                    editText.setText(mascara)
                    editText.setSelection(mascara.length)
                }
            }
            return textWatcher
        }

        private fun getDefaultMask(str: String): String {
            var defaultMask = maskCPF
            if (str.length > 11) {
                defaultMask = maskCNPJ
            }
            return defaultMask
        }
    }
}