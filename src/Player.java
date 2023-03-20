import javax.sound.sampled.*;
import java.io.*;

public class Player implements Runnable {

    private String soundFile;

    public Player(String soundFile) {
        String directory = "./sounds/";
        this.soundFile = directory.concat(soundFile);
    }

    @Override
    // override Runnable's run function to play sound as a thread
    public void run() {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.start();
            try {
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            } catch (InterruptedException e) {
                clip.stop();
            }
            clip.stop();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }


    }



    // function to play sound not in a thread
    public void play() {
        run();
    }
}