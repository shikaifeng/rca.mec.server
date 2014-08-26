function convertCurrency(currencyDigits) {  
	// Constants:  
	var MAXIMUM_NUMBER = 99999999999.99;  
	// Predefine the radix characters and currency symbols for output:  
	var CN_ZERO = "零";  
	var CN_ONE = "壹";  
	var CN_TWO = "贰";  
	var CN_THREE = "叁";  
	var CN_FOUR = "肆";  
	var CN_FIVE = "伍";  
	var CN_SIX = "陆";  
	var CN_SEVEN = "柒";  
	var CN_EIGHT = "捌";  
	var CN_NINE = "玖";  
	var CN_TEN = "拾";  
	var CN_HUNDRED = "佰";  
	var CN_THOUSAND = "仟";  
	var CN_TEN_THOUSAND = "万";  
	var CN_HUNDRED_MILLION = "亿";  
	var CN_SYMBOL = "";  
	var CN_DOLLAR = "元";  
	var CN_TEN_CENT = "角";  
	var CN_CENT = "分";  
	var CN_INTEGER = "整";  
	  
	// Variables:  
	var integral; // Represent integral part of digit number.  
	var decimal; // Represent decimal part of digit number.  
	var outputCharacters; // The output result.  
	var parts;  
	var digits, radices, bigRadices, decimals;  
	var zeroCount;  
	var i, p, d;  
	var quotient, modulus;  
	  
	// Validate input string:  
	currencyDigits = currencyDigits.toString();  
	if (currencyDigits == "") {  
	  alert("输入为空值!");  
	  return "";  
	}  
	if (currencyDigits.match(/[^,.\d]/) != null) {  
	  alert("输入非法字符!");  
	  return "";  
	}  
	if ((currencyDigits).match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {  
	  alert("不是有效数字!");  
	  return "";  
	}  
	  
	// Normalize the format of input digits:  
	currencyDigits = currencyDigits.replace(/,/g, ""); // Remove comma delimiters.  
	currencyDigits = currencyDigits.replace(/^0+/, ""); // Trim zeros at the beginning.  
	// Assert the number is not greater than the maximum number.  
	if (Number(currencyDigits) > MAXIMUM_NUMBER) {  
	  alert("输入数字太大!");  
	  return "";  
	}  
	  
	// Process the coversion from currency digits to characters:  
	// Separate integral and decimal parts before processing coversion:  
	parts = currencyDigits.split(".");  
	if (parts.length > 1) {  
	  integral = parts[0];  
	  decimal = parts[1];  
	  // Cut down redundant decimal digits that are after the second.  
	  decimal = decimal.substr(0, 2);  
	}  
	else {  
	  integral = parts[0];  
	  decimal = "";  
	}  
	// Prepare the characters corresponding to the digits:  
	digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);  
	radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);  
	bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);  
	decimals = new Array(CN_TEN_CENT, CN_CENT);  
	// Start processing:  
	outputCharacters = "";  
	// Process integral part if it is larger than 0:  
	if (Number(integral) > 0) {  
	  zeroCount = 0;  
	  for (i = 0; i < integral.length; i++) {  
	   p = integral.length - i - 1;  
	   d = integral.substr(i, 1);  
	   quotient = p / 4;  
	   modulus = p % 4;  
	   if (d == "0") {  
	    zeroCount++;  
	   }  
	   else {  
	    if (zeroCount > 0)  
	    {  
	     outputCharacters += digits[0];  
	    }  
	    zeroCount = 0;  
	    outputCharacters += digits[Number(d)] + radices[modulus];  
	   }  
	   if (modulus == 0 && zeroCount < 4) {  
	    outputCharacters += bigRadices[quotient];  
	   }  
	  }  
	  outputCharacters += CN_DOLLAR;  
	}  
	// Process decimal part if there is:  
	if (decimal != "") {  
	  for (i = 0; i < decimal.length; i++) {  
	   d = decimal.substr(i, 1);  
	   if (d != "0") {  
	    outputCharacters += digits[Number(d)] + decimals[i];  
	   }  
	  }  
	}  
	// Confirm and return the final output string:  
	if (outputCharacters == "") {  
	  outputCharacters = CN_ZERO + CN_DOLLAR;  
	}  
	if (decimal == "") {  
	  outputCharacters += CN_INTEGER;  
	}  
	outputCharacters = CN_SYMBOL + outputCharacters;  
	return outputCharacters;  
}

(function () {
    var calc = {
        /*
        函数，加法函数，用来得到精确的加法结果  
        说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
        参数：arg1：第一个加数；arg2第二个加数；d要保留的小数位数（可以不传此参数，如果不传则不处理小数位数）
        调用：Calc.Add(arg1,arg2,d)  
        返回值：两数相加的结果
        */
        Add: function (arg1, arg2) {
            arg1 = arg1.toString(), arg2 = arg2.toString();
            var arg1Arr = arg1.split("."), arg2Arr = arg2.split("."), d1 = arg1Arr.length == 2 ? arg1Arr[1] : "", d2 = arg2Arr.length == 2 ? arg2Arr[1] : "";
            var maxLen = Math.max(d1.length, d2.length);
            var m = Math.pow(10, maxLen);
            var result = Number(((arg1 * m + arg2 * m) / m).toFixed(maxLen));
            var d = arguments[2];
            return typeof d === "number" ? Number((result).toFixed(d)) : result;
        },
        /*
        函数：减法函数，用来得到精确的减法结果  
        说明：函数返回较为精确的减法结果。 
        参数：arg1：第一个加数；arg2第二个加数；d要保留的小数位数（可以不传此参数，如果不传则不处理小数位数
        调用：Calc.Sub(arg1,arg2)  
        返回值：两数相减的结果
        */
        Sub: function (arg1, arg2) {
            return Calc.Add(arg1, -Number(arg2), arguments[2]);
        },
        /*
        函数：乘法函数，用来得到精确的乘法结果  
        说明：函数返回较为精确的乘法结果。 
        参数：arg1：第一个乘数；arg2第二个乘数；d要保留的小数位数（可以不传此参数，如果不传则不处理小数位数)
        调用：Calc.Mul(arg1,arg2)  
        返回值：两数相乘的结果
        */
        Mul: function (arg1, arg2) {
            var r1 = arg1.toString(), r2 = arg2.toString(), m, resultVal, d = arguments[2];
            m = (r1.split(".")[1] ? r1.split(".")[1].length : 0) + (r2.split(".")[1] ? r2.split(".")[1].length : 0);
            resultVal = Number(r1.replace(".", "")) * Number(r2.replace(".", "")) / Math.pow(10, m);
            return typeof d !== "number" ? Number(resultVal) : Number(resultVal.toFixed(parseInt(d)));
        },
        /*
        函数：除法函数，用来得到精确的除法结果  
        说明：函数返回较为精确的除法结果。 
        参数：arg1：除数；arg2被除数；d要保留的小数位数（可以不传此参数，如果不传则不处理小数位数)
        调用：Calc.Div(arg1,arg2)  
        返回值：arg1除于arg2的结果
        */
        Div: function (arg1, arg2) {
            var r1 = arg1.toString(), r2 = arg2.toString(), m, resultVal, d = arguments[2];
            m = (r2.split(".")[1] ? r2.split(".")[1].length : 0) - (r1.split(".")[1] ? r1.split(".")[1].length : 0);
            resultVal = Number(r1.replace(".", "")) / Number(r2.replace(".", "")) * Math.pow(10, m);
            return typeof d !== "number" ? Number(resultVal) : Number(resultVal.toFixed(parseInt(d)));
        }
    };
    window.Calc = calc;
}());