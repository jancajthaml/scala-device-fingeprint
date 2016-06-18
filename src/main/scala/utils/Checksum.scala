package utils


object Checksum {

  def calculate(a:String) = {
    //our seed (checksum offset to keep the numbers small)
    var hval:Int = 0x811c9dc5
    for (c <- a) {
      hval ^= c&0xFF
      hval += (hval << 1) + (hval << 4) + (hval << 7) + (hval << 8) + (hval << 24)
    }
    hval.toLong & 0x00000000ffffffffL
  }

}