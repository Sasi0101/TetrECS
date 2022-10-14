package uk.ac.soton.comp1206.utilities;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Multimedia {

    /**
     * Declaring the variables we are going to use
     */
    private MediaPlayer musicPlayer;

    /**
     * Declaring a playAudio method that lets us play an audio
     * @param file the name of the audio file
     */
    public void playAudio(String file) {
        String toPlay = Multimedia.class.getResource("/sounds/" + file).toExternalForm();
        try {
            Media play = new Media(toPlay);
            MediaPlayer audioPlayer = new MediaPlayer(play);
            audioPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Making a playMusic method that lets us play a music
     * @param file the name of the music file
     */
    public void playMusic(String file) {
        String toPlay = Multimedia.class.getResource("/music/" + file).toExternalForm();
        try {
            Media play = new Media(toPlay);
            musicPlayer = new MediaPlayer(play);
            musicPlayer.setCycleCount(Integer.MAX_VALUE);
            musicPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returning the musicPlayer
     * @return this musicPlayer
     */
    public MediaPlayer getMusicPlayer(){
        return musicPlayer;
    }
}

