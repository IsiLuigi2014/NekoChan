package koyami.nekochan.util;

import koyami.nekochan.commands.control.MessageData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static Document getDocument(String url) {
        Connection conn = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        Document document = null;
        try {
            document = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.logError("Failed to Connect to Website");
            // handle error
        }
        return document;
    }

    public static Document getComplexDocument(String url) {
        Connection.Response res = null;
        try {
            res = Jsoup.connect(url)
                    .followRedirects(false)
                    .timeout(0)
                    .method(Connection.Method.GET)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                    .execute();

            String location = res.header("Location");

            res = Jsoup.connect(url)
                    .timeout(0)
                    .data("is_check", "1")
                    .method(Connection.Method.POST)
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Referer", location)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document document = null;
        try {
            document = res.parse();
        } catch (IOException e) {
            e.printStackTrace();
            // handle error
        }
        return document;
    }

    public static Member getMemberFromMessage(MessageData data, String[] args) {
        Member member;
        try {
            member = data.guild.getMemberById(data.message.getMentions().getUsers().get(0).getIdLong());
        } catch (Exception e) {
            try {
                member = data.guild.getMemberById(Long.parseLong(args[1]));
            } catch (Exception f) {
                member = data.member;
            }
        }
        return member;
    }

    public static EmbedBuilder constructNewsEmbed(String title, String description, String newsUrl, String imageUrl, String time, String author) {
        return new EmbedBuilder().setAuthor(title, newsUrl).setDescription(description).setImage(imageUrl).setFooter(String.format("%s | %s", time, author)).setColor(Util.randomColor());
    }

    public static EmbedBuilder constructClassicEmbed(String title, String imageUrl, String footer, String description, Color color) {
        //String sas = new String("82e214");
        if (color == null) color = randomColor();
        if (imageUrl.equals(""))
            return new EmbedBuilder().setColor(color).setAuthor(title).setDescription(description).setFooter(footer);
        else
            return new EmbedBuilder().setColor(color).setAuthor(title).setDescription(description).setFooter(footer).setImage(imageUrl);
    }

    public static void writeFile(String input, String filepath) throws IOException {
        Files.write(Paths.get(filepath), input.getBytes());
    }

    public static String readFile(String filepath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)));
    }

    public static String[] readAllFilenames(String direcoryPath) throws IOException {
        List<String> results = new ArrayList<>();
        File[] files = new File(direcoryPath).listFiles();
        if (files == null) return new String[]{};
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return (String[]) Arrays.stream(results.toArray()).toArray();
    }

    public static boolean writeFileAppend(String input, String filepath) {
        try {
            Path path = Path.of(filepath);
            if (!Files.exists(path)) Files.createFile(path);
            Files.write(path, input.getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean logDebugFileAppend(String input) {
        String file = "debug.txt";
        return writeFileAppend(String.format("%s: %s\n",getTime(),input),file);
    }

    public static boolean fileExist(String filepath) {
        File f = new File(filepath);
        if (f.exists() && !f.isDirectory()) return true;
        return false;
    }

    public static void sendEmbedMessage(TextChannel channel, EmbedBuilder embed) {
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static Message sendMessage(TextChannel channel, String message) {
        return channel.sendMessage(message).complete();
    }

    public static String readHTML(String url) {
        StringBuilder html = new StringBuilder();
        try (InputStream input = new URL(url).openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            int c;
            while ((c = reader.read()) != -1) {
                html.append((char) c);
            }
            return html.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getTime() {
        return DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }
    public static int random(int timesx) {
        return (int) (Math.random() * timesx);
    }

    public static Color randomColor() {
        return new Color((int) (Math.random() * 0x1000000));
    }
}
