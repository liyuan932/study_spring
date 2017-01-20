import com.beust.jcommander.internal.Maps;
import com.tiro.study.spring.config.RootConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.testng.annotations.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author wb-liyuan.j
  @date 2017-01-19
 */
@ContextConfiguration(classes = RootConfig.class)
public class BaseTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private VelocityEngine velocity;
    @Autowired
    private Configuration freeMarker;
    @Autowired
    private SpringTemplateEngine thymeleaf;
    
    private static  final String FROM = "18758595684@163.com";
    private static  final String TO = "liyuan932a@163.com";

    @Test
    public void sendSimpleMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(TO);
        message.setSubject("发送简单文本");
        message.setText("测试");
        mailSender.send(message);
    }

    /**
     * 发送附件及html文本，解决中文编码问题
     * @throws Exception
     */
    @Test
    public void sendMimeMail() throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(FROM);
        helper.setTo(TO);
        helper.setSubject("发送附件及html文本");

        String htmlText = "<a href='http://www.baidu.com'>百度一下</a>";
        helper.setText(new String(htmlText.getBytes("utf-8"), "ISO8859-1"),true);

        ClassPathResource attach = new ClassPathResource("mail/mail.properties");
        helper.addAttachment("mail/mail.properties",attach);
        mailSender.send(message);
    }

    /**
     * 使用contentId发图片，发图片+超链接报554
     * @throws Exception
     */
    @Test
    public void sendImgMail() throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(FROM);
        helper.setTo(TO);
        helper.setSubject("发送图片");

        String htmlText = "<html><body><img src='https://www.baidu.com/img/bd_logo1.png'><img "
            + "src='cid:bdLogo'></body>发送图片</html>";
        helper.setText(new String(htmlText.getBytes("utf-8"), "ISO8859-1"),true);

        ClassPathResource img = new ClassPathResource("mail/bd_logo1.png");
        helper.addInline("bdLogo",img);

        mailSender.send(message);
    }

    /**
     * 使用velocity模板发送邮件
     * @throws Exception
     */
    @Test
    public void sendMailWithVelocityTemplate() throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(FROM);
        helper.setTo(TO);
        helper.setSubject("使用velocity模板发送邮件");

        Map<String,Object> model = Maps.newHashMap();
        model.put("name","李远");
        model.put("text","大家好!!");
        String mailText = VelocityEngineUtils.mergeTemplateIntoString(velocity, "mail/mailTemplate.vm",model);
        helper.setText(new String(mailText.getBytes("utf-8"), "ISO8859-1"),true);
        ClassPathResource img = new ClassPathResource("mail/bd_logo1.png");
        helper.addInline("bdLogo",img);

        mailSender.send(message);
    }

    /**
     * 使用FreeMarker模板发送邮件
     * @throws Exception
     */
    @Test
    public void sendMailWithFreeMarkerTemplate() throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(FROM);
        helper.setTo(TO);
        helper.setSubject("使用FreeMarker模板发送邮件");

        Map<String,Object> model = Maps.newHashMap();
        model.put("name","李远");
        model.put("text","大家好!!");
        Template template = freeMarker.getTemplate("mailTemplate.ftl");
        String mailText = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
        helper.setText(new String(mailText.getBytes("utf-8"), "ISO8859-1"),true);
        ClassPathResource img = new ClassPathResource("mail/bd_logo1.png");
        helper.addInline("bdLogo",img);

        mailSender.send(message);
    }

    /**
     * 使用thymeleaf模板发送邮件
     * @throws Exception
     */
    @Test
    public void sendMailWithThymeleafTemplate() throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(FROM);
        helper.setTo(TO);
        helper.setSubject("使用Thymeleaf模板发送邮件");

        Context context = new Context();
        context.setVariable("name","李远");
        context.setVariable("text","大家好!!");
        String mailText = thymeleaf.process("mailTemplate.html",context);
        helper.setText(new String(mailText.getBytes("utf-8"), "ISO8859-1"),true);
        ClassPathResource img = new ClassPathResource("mail/bd_logo1.png");
        helper.addInline("bdLogo",img);

        mailSender.send(message);
    }
}
