package org.ffpy.easyspider.core.qidian;

public class Fans {
    private Recommend recommend;
    private Reward reward;

    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "Fans{" +
                "recommend=" + recommend +
                ", reward=" + reward +
                '}';
    }
}
