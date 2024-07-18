// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2024
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.oxi.one;

import java.util.List;

import de.mossgrabers.framework.configuration.AbstractConfiguration;
import de.mossgrabers.framework.configuration.IEnumSetting;
import de.mossgrabers.framework.configuration.ISettingsUI;
import de.mossgrabers.framework.controller.valuechanger.IValueChanger;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.constants.Capability;
import de.mossgrabers.framework.daw.midi.ArpeggiatorMode;
import de.mossgrabers.framework.view.Views;


/**
 * The configuration settings for OXI One.
 *
 * @author Jürgen Moßgraber
 */
public class OxiOneConfiguration extends AbstractConfiguration
{
    private static final Views [] PREFERRED_NOTE_VIEWS =
    {
        Views.PLAY,
        Views.PIANO,
        Views.DRUM64,
        Views.DRUM4,
        Views.SEQUENCER,
        Views.POLY_SEQUENCER
    };

    /** Should all track states be colored? */
    public static final Integer   COLOR_TRACK_STATES   = Integer.valueOf (100);

    private boolean               colorTrackStates;


    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param valueChanger The value changer
     * @param arpeggiatorModes The available arpeggiator modes
     */
    public OxiOneConfiguration (final IHost host, final IValueChanger valueChanger, final List<ArpeggiatorMode> arpeggiatorModes)
    {
        super (host, valueChanger, arpeggiatorModes);
    }


    /** {@inheritDoc} */
    @Override
    public void init (final ISettingsUI globalSettings, final ISettingsUI documentSettings)
    {
        ///////////////////////////
        // Scale

        this.activateScaleSetting (documentSettings);
        this.activateScaleBaseSetting (documentSettings);
        this.activateScaleInScaleSetting (documentSettings);
        this.activateScaleLayoutSetting (documentSettings);

        ///////////////////////////
        // Note Repeat

        this.activateNoteRepeatSetting (documentSettings);

        ///////////////////////////
        // Session

        this.activateSelectClipOnLaunchSetting (globalSettings);
        this.activateDrawRecordStripeSetting (globalSettings);
        this.activateActionForRecArmedPad (globalSettings);

        ///////////////////////////
        // Transport

        this.activateBehaviourOnStopSetting (globalSettings);
        this.activateBehaviourOnPauseSetting (globalSettings);

        // Corrected label (removed automation)
        final IEnumSetting flipRecordSetting = globalSettings.getEnumSetting ("Flip arranger and clip record", CATEGORY_TRANSPORT, ON_OFF_OPTIONS, ON_OFF_OPTIONS[0]);
        flipRecordSetting.addValueObserver (value -> {
            this.flipRecord = "On".equals (value);
            this.notifyObservers (FLIP_RECORD);
        });
        this.isSettingActive.add (FLIP_RECORD);

        ///////////////////////////
        // Play and Sequence

        this.activateAccentActiveSetting (globalSettings);
        this.activateAccentValueSetting (globalSettings);
        this.activateQuantizeAmountSetting (globalSettings);
        this.activateMidiEditChannelSetting (documentSettings);
        this.activatePreferredNoteViewSetting (globalSettings, PREFERRED_NOTE_VIEWS);
        this.activateStartWithSessionViewSetting (globalSettings);

        ///////////////////////////
        // Drum Sequencer

        if (this.host.supports (Capability.HAS_DRUM_DEVICE))
            this.activateTurnOffEmptyDrumPadsSetting (globalSettings);
        this.activateUseCombinationButtonToSoundSetting (globalSettings);

        ///////////////////////////
        // Workflow

        this.activateExcludeDeactivatedItemsSetting (globalSettings);
        this.activateNewClipLengthSetting (globalSettings);
        this.activateKnobSpeedSetting (globalSettings);

        // Color all track states in mixer view
        final IEnumSetting excludeDeactivatedItemsSetting = globalSettings.getEnumSetting ("Color all track states (mute, solo, rec arm)", CATEGORY_WORKFLOW, ON_OFF_OPTIONS, ON_OFF_OPTIONS[1]);
        excludeDeactivatedItemsSetting.addValueObserver (value -> {
            this.colorTrackStates = ON_OFF_OPTIONS[1].equals (value);
            this.notifyObservers (COLOR_TRACK_STATES);
        });
        this.isSettingActive.add (COLOR_TRACK_STATES);
    }


    /**
     * Should all track states be colored?
     * 
     * @return True if all color track states should be colored
     */
    public boolean isColorTrackStates ()
    {
        return this.colorTrackStates;
    }
}
