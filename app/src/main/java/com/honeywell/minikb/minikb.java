package com.honeywell.minikb;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class minikb extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard_qwerty;
    private Keyboard keyboard_symbols;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard_qwerty = new Keyboard(this, R.xml.qwerty);
        keyboard_symbols = new Keyboard(this, R.xml.symbols);
        kv.setKeyboard(keyboard_symbols);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_MODE_CHANGE:
//                Toast.makeText(this, "Hemos pulsado CHANGE", Toast.LENGTH_SHORT).show();
                if (kv.getKeyboard().equals(keyboard_qwerty)){
                    kv.setKeyboard(keyboard_symbols);
                }
                else{
                    kv.setKeyboard(keyboard_qwerty);
                }
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.sendKeyEvent(new KeyEvent(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL,0,0,0,0,
                        KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE,
                        InputDevice.SOURCE_CLASS_BUTTON));
                ic.sendKeyEvent(new KeyEvent(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100,
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL,0,0,0,0,
                        KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE,
                        InputDevice.SOURCE_CLASS_BUTTON));
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard_qwerty.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
//                Toast.makeText(this, "Enter", Toast.LENGTH_SHORT).show();
                ic.sendKeyEvent(new KeyEvent(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER,0,0,0,0,
                        KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE | KeyEvent.FLAG_EDITOR_ACTION,
                        InputDevice.SOURCE_KEYBOARD));
                ic.sendKeyEvent(new KeyEvent(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100,
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER,0,0,0,0,
                        KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE | KeyEvent.FLAG_EDITOR_ACTION,
                        InputDevice.SOURCE_KEYBOARD));
                break;
            case 131:
                ic.sendKeyEvent(new KeyEvent(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_F1,0,0,0,0,0,
                        InputDevice.SOURCE_CLASS_BUTTON));
                ic.sendKeyEvent(new KeyEvent(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100,
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_F1,0,0,0,0,0,
                        InputDevice.SOURCE_CLASS_BUTTON));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
