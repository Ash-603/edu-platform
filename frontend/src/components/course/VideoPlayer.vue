<template>
    <div class="video-player-container">
      <div v-if="loading" class="video-loading">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
        <div class="mt-2">Loading video...</div>
      </div>
      
      <video
        ref="videoPlayer"
        class="video-js vjs-big-play-centered"
        controls
        preload="auto"
        :poster="poster"
        data-setup="{}"
        @timeupdate="onTimeUpdate"
      >
        <source :src="src" :type="type">
        <p class="vjs-no-js">
          To view this video please enable JavaScript, and consider upgrading to a
          web browser that supports HTML5 video
        </p>
      </video>
    </div>
  </template>
  
  <script>
  import videojs from 'video.js'
  import 'video.js/dist/video-js.css'
  
  export default {
    props: {
      src: {
        type: String,
        required: true
      },
      poster: {
        type: String,
        default: ''
      },
      type: {
        type: String,
        default: 'video/mp4'
      },
      lessonId: {
        type: String,
        required: true
      },
      courseId: {
        type: String,
        required: true
      },
      autoSaveProgress: {
        type: Boolean,
        default: true
      }
    },
    
    data() {
      return {
        player: null,
        loading: true,
        currentTime: 0,
        progressSaveInterval: null
      }
    },
    
    mounted() {
      this.initializePlayer()
    },
    
    beforeDestroy() {
      if (this.player) {
        this.saveProgress()
        this.player.dispose()
      }
      
      if (this.progressSaveInterval) {
        clearInterval(this.progressSaveInterval)
      }
    },
    
    methods: {
      initializePlayer() {
        this.player = videojs(this.$refs.videoPlayer, {
          controls: true,
          autoplay: false,
          fluid: true,
          playbackRates: [0.5, 1, 1.25, 1.5, 2]
        }, () => {
          this.loading = false
          this.player.on('ended', this.onVideoEnded)
          
          // 恢复上次播放位置
          this.loadProgress()
          
          // 定期保存进度
          if (this.autoSaveProgress) {
            this.progressSaveInterval = setInterval(() => {
              this.saveProgress()
            }, 30000) // 每30秒保存一次
          }
        })
      },
      
      onTimeUpdate() {
        this.currentTime = this.player.currentTime()
      },
      
      onVideoEnded() {
        this.$emit('completed')
        this.saveProgress(true)
      },
      
      async saveProgress(completed = false) {
        if (!this.currentTime) return
        
        try {
          await this.$axios.post('/courses/progress', {
            courseId: this.courseId,
            lessonId: this.lessonId,
            progress: this.currentTime,
            completed
          })
        } catch (error) {
          console.error('Failed to save progress:', error)
        }
      },
      
      async loadProgress() {
        try {
          const response = await this.$axios.get(`/courses/${this.courseId}/lessons/${this.lessonId}/progress`)
          if (response.data && response.data.progress) {
            this.player.currentTime(response.data.progress)
          }
        } catch (error) {
          console.error('Failed to load progress:', error)
        }
      }
    }
  }
  </script>
  
  <style>
  .video-player-container {
    position: relative;
    width: 100%;
  }
  
  .video-loading {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    z-index: 1;
  }
  </style>