package me.ichun.mods.clef.common.util.abc.play.components;

import me.ichun.mods.clef.common.util.abc.play.PlayedNote;
import me.ichun.mods.clef.common.util.abc.play.Track;

import java.util.ArrayList;
import java.util.HashMap;

public class Chord extends Note
{
    public ArrayList<Note> notes = new ArrayList<>();

    @Override
    public boolean playNote(Track track, ArrayList<PlayedNote> playedNotes, int currentProg)
    {
        for(Note note : notes)
        {
            note.playNote(track, playedNotes, currentProg);
        }
        return true;
    }

    @Override
    public boolean setup(double[] info, HashMap<Integer, Integer> keyAccidentals)
    {
        for(Note note : notes)
        {
            note.duration *= duration;
            note.setup(info, keyAccidentals);
        }
        return true;
    }
}
