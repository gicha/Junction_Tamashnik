package com.muki;

class TimeViewer extends Thread {
    private Core core;
    private MainActivity mActivity;
    private int lastImage = -1;

    private Api mApi;

    TimeViewer(MainActivity ma) {
        core = Core.getInstance();
        mApi = Api.getInstance();
        mActivity = ma;
        start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int fhealth = 0;
            int fsatiety = 0;
            if (core.player.health != 0) fhealth = --core.player.health;
            if (core.player.satiety != 0) fsatiety = --core.player.satiety;
            final int nowHealth = fhealth;
            final int nowSatiety = fsatiety;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivity.healthStatus.setText(nowHealth + " ");
                    mActivity.satietyStatus.setText(nowSatiety + " ");
                }
            });


            if ((nowHealth > 75) && (nowSatiety > 75)) {
                if (lastImage == R.drawable.d100) continue;
                changeAllImage(R.drawable.d100);
                lastImage = R.drawable.d100;
            }
            if (((nowHealth <= 75) || (nowSatiety <= 75)) && (nowHealth > 25) && (nowSatiety > 25)) {
                if (lastImage == R.drawable.d75) continue;
                changeAllImage(R.drawable.d75);
                lastImage = R.drawable.d75;
            }
            if (((nowHealth <= 25) || (nowSatiety <= 25)) && (nowHealth > 0) && (nowSatiety > 0)) {
                if (lastImage == R.drawable.d25) continue;
                changeAllImage(R.drawable.d25);
                lastImage = R.drawable.d25;
            }
            if ((nowHealth == 0) || (nowSatiety == 0)) {
                if (lastImage == R.drawable.d0) continue;
                changeAllImage(R.drawable.d0);
                lastImage = R.drawable.d0;

            }


        }
    }

    private void changeAllImage(final int idImage) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.pimage.setBackgroundResource(idImage);
            }
        });
        mApi.crop(idImage);
        mApi.setupImage();
        mApi.send();
    }
}
