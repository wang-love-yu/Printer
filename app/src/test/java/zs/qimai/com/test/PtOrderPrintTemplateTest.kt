package zs.qimai.com.test

import org.junit.Assert.*
import org.junit.Test
@TestAnimation1(id = "123",name = "456")
class PtOrderPrintTemplateTest{
   @Test
    fun testAnimation(){
      var hasAnimation =  TestAnimator::class.java.isAnnotationPresent(TestAnimation1::class.java)

        if (hasAnimation){
            var testAnimation1 = PtOrderPrintTemplateTest::class.java.getAnnotation(TestAnimation1::class.java)
            var list = PtOrderPrintTemplateTest::class.java.annotations
            for (i in list)
            println("id= ${testAnimation1?.id} value = ${testAnimation1?.name}")


        }



    }

}