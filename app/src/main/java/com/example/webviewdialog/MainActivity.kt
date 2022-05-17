package com.example.webviewdialog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.mindlinker.webviewdialog.dialog.WebviewDialaog
import com.mindlinker.webviewdialog.dialog.WebviewDialog2
import com.mindlinker.webviewdialog.dialog.WebviewDialog3


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.content_main)

        val html = "\n" +
                "<font color=\"#222222\">欢迎使用小游戏APP。在您使用本APP前，请仔细阅读<font color=\"#FF9800\"><a href= \"https://baidu.com/agreement.html\">《用户协议》</a ></font>和<font color=\"#FF9800\"><a href=\"https://baidu.com/privacy.html\">《隐私政策》</a ></font>的全部内容，同意并接受全部条款后开始使用我们的产品和服务。我们会严格按照政策内容使用和保护您的个人信息，感谢您的信任。<br/><br/>若您同意以上用户协议和隐私协议保护政策，请点击“同意”并开始使用我们的产品和服务。<br/><br/><a href=\"tel:4001688836\" >400 168 8836 </a></font>"

        val text = findViewById<TextView>(R.id.button)
        text.setOnClickListener {
            val dialog = WebviewDialaog(
                this,
                "Dialog加载网页",
                "file:///android_asset/js.html",
//                "https://www.baidu.com/",
                onConfirmCallback = {},
                onCancelCallback = {}
            )
            dialog.show()
//            dialog.dismiss()
        }

        val html6 =
            ("<html><head><title>TextView使用HTML</title></head><body><p><strong>强调</strong></p><p><em>斜体</em></p>"
                    + "<p><a href=\"http://www.dreamdu.com/xhtml/\">超链接HTML入门</a>学习HTML!</p><p><font color=\"#aabb00\">颜色1"
                    + "</p><p><font color=\"#00bbaa\">颜色2</p><p><font color=\"#aabb00\">颜色1"
                    + "</p><p><font color=\"#00bbaa\">颜色2</p><p><font color=\"#aabb00\">颜色1"
                    + "</p><p><font color=\"#00bbaa\">颜色2</p><p><font color=\"#aabb00\">颜色1"
                    + "</p><p><font color=\"#00bbaa\">颜色2</p><p><font color=\"#aabb00\">颜色1"
                    + "</p><p><font color=\"#00bbaa\">颜色2</p><h1>标题1</h1><h3>标题2</h3><h6>标题3</h6><p>大于>小于<</p><p>"
                    + "下面是网络图片</p><img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></body></html>")


        val html3 = "<html><body><p>检测到当前会议可能涉嫌网络诈骗，已为<br/>您自动退出会议。如需帮助，请联系：<br/>" +
                "<br/><br/><font color=\"#D95F5F\"><a href=\"sms:4001688836\" ><big>400 168 8836 </big></a></font>" +
                "<br/><br/><font color=\"#005F5F\"><h1><a href=\"https://baidu.com/agreement.html\" >https:www.baidu.com </a></h1></size></font>" +
                "</p></body></html>" +
                 "<font color=\"#00005F\"><a href=\"tel:4001688836\" >400 168 8836 </a></font>"

        val secondInfo = ("<font color=\"#B7B7B7\">" + "空余位: " + "</font>"
                + "<font color=\"#D95F5F\">" + "1072" + "</font>"
                + "<font color=\"#B7B7B7\">" + " 总车位: " + "1162" + "</font>")

        val html5 =
            "<html><body style='font-size:15px;color:#57AFFC'><p>18888元大礼包免费送给你<br />等等，还没完，还有<br /><font size='100px'><size>100元</siza></font>返现，用不用随你</p></body></html>"

//        <br/><br/><img src="https://img0.pconline.com.cn/pconline/1808/06/11566885_13b_thumb.jpg"></img>
        val text2 = findViewById<TextView>(R.id.button2)
        text2.setOnClickListener {
            val dialog2 = com.mindlinker.webviewdialog.dialog.WebviewDialog2(
                this,
                "",
                html + html3,
                onConfirmCallback = {},
                onCancelCallback = {}
            )
            dialog2.show()
        }
        val text3 = findViewById<TextView>(R.id.button3)
        text3.setOnClickListener {
            val dialog3 = com.mindlinker.webviewdialog.dialog.WebviewDialog3(
                this,
                "",
                html3 + html3 + html3,
                onConfirmCallback = {},
                onCancelCallback = {}
            )
            dialog3.show()
        }

        val text4 = findViewById<TextView>(R.id.button4)
        text4.setOnClickListener {
//            Toast.makeText(this, "hhhhhh", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, FragmentActivity::class.java))
        }
//        StaticLayoutManager.getInstance().initLayout(this@MainActivity, TestSpan.getSpanString(), TestSpan.getLongSpanString());

    }



}