class Forth {
    var plus = "+"
    var minus = "-"
    var mul = "*"
    var div = "/"
    var dup = "dup"
    var drop = "drop"
    var swap = "swap"
    var over = "over"
    var rewrite = false
    var numbers: MutableList<Int> = mutableListOf()
    var size = 0
    get() {
         return numbers.size
    }
    val def = ":"
    val map: HashMap<String, String> = HashMap<String,String>()
    
    fun evaluate(vararg line: String): List<Int> {
        
        for (l in line){
            val originalLine: List<String> = l.split(" ").map{a -> a.lowercase()}
            val list: List<String> = if (originalLine[0] != this.def) {
                     originalLine.map{a-> mapping(map,a)}.reduce{a,b -> "$a $b"}.split(" ")
                } else {
                    originalLine
                }

                
            

            for (token in list){
               
                if(isNumber(token)){
                    numbers.add(token.toInt())
                }
                else{
                    
                    
                    
                    when(token.lowercase()){
                        this.plus -> {
                            checkEmpty(list.size - 1)
                            val (first, second) = removeNumbers(numbers, this.size - 2, this.size -1)
                            numbers.add(first + second)
                        }

                        this.minus -> {
                            checkEmpty(list.size - 1)
                            val (first, second) = removeNumbers(numbers, this.size - 2, this.size -1)
                            numbers.add(first - second)
                        }

                        this.mul -> {
                            checkEmpty(list.size - 1)
                            val (first, second) = removeNumbers(numbers, this.size - 2, this.size -1)
                            numbers.add(first * second)
                        }

                        this.div -> {
                            checkEmpty(list.size - 1)
                            val (first, second) = removeNumbers(numbers, this.size - 2, this.size -1)
                            if(second == 0){
                                throw IllegalArgumentException("divide by zero")
                            }
                            numbers.add(first / second)
                        }

                        this.dup -> {
                            checkEmpty(list.size - 1)
                            numbers.add(numbers[size - 1])
                        }

                        this.drop -> {
                            checkEmpty(list.size - 1)
                            numbers.removeAt(size-1)
                        }

                        this.swap -> {
                            checkEmpty(list.size - 1)
                            checkDouble(numbers, this.size -1) 
                            val temp = numbers[this.size - 2]
                            numbers[this.size - 2] = numbers[this.size - 1]
                            numbers[this.size - 1] = temp 
                        }

                        this.over -> {
                            checkEmpty(list.size - 1)
                            checkDouble(numbers,  this.size -1) 
                            numbers.add(numbers[this.size - 2] )
                        }

                        this.def -> {
                            val new_word = list[1]
                            if (isNumber(new_word)){
                                throw IllegalArgumentException("illegal operation")
                            }
                            val mapping = list.slice(2.. list.size -2).map{a -> mapping(map,a)}.reduce{a,b -> a + " " + b}
                            map.put(new_word, mapping)
                            break
                        }
                        else ->{ 
                            throw IllegalArgumentException("undefined operation")
                        }
                    }
                }
            }
        }   
        return this.numbers 
    }
    
}


fun mapping(map: HashMap<String, String>, key: String ):String {
    var mappedCommand: String 
    if(map.containsKey(key)){
        mappedCommand = mapping(map, map.get(key)?:"")
    }
    else {
        mappedCommand = "$key"
    }
    return mappedCommand
}

fun checkDouble(list: MutableList<Int>, idx2: Int){
    if(list.size == 1){
        throw IllegalArgumentException("only one value on the stack")
    }
    if(idx2 >= list.size){
        throw IllegalArgumentException("only one value on the stack")
    }
    
}


fun removeNumbers(list: MutableList<Int>, idx1: Int, idx2: Int): Pair<Int,Int>{
    

    if(list.size == 1){
        throw IllegalArgumentException("only one value on the stack")
    }
    if(idx2 >= list.size){
        throw IllegalArgumentException("only one value on the stack")
    }
    val second = list.removeAt(idx2)
    val first = list.removeAt(idx1)
    return Pair(first, second)
}


fun isNumber(s: String): Boolean {
    return try {
        s.toInt()
        true
    } catch (ex: NumberFormatException) {
        false
    }
}


fun checkEmpty(size: Int){
    if (size == 0){
        throw IllegalArgumentException("empty stack")
    }
}

