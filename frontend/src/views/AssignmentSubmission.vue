<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-card>
            <v-card-title class="headline">
              <v-btn icon class="mr-2" :to="`/courses/${courseId}`">
                <v-icon>mdi-arrow-left</v-icon>
              </v-btn>
              {{ assignment.title }}
            </v-card-title>

            <v-card-subtitle>
              <div class="d-flex align-center">
                <v-icon small class="mr-1">mdi-clock-outline</v-icon>
                <span>Due: {{ formatDate(assignment.dueDate) }}</span>
                <v-chip
                  x-small
                  class="ml-2"
                  :color="getDueStatusColor()"
                  text-color="white"
                >
                  {{ getDueStatus() }}
                </v-chip>
              </div>
            </v-card-subtitle>

            <v-divider></v-divider>

            <v-card-text>
              <v-row>
                <v-col cols="12" md="8">
                  <h3 class="text-h6 mb-3">Assignment Instructions</h3>
                  <div v-html="assignment.description"></div>

                  <div
                    v-if="
                      assignment.resources && assignment.resources.length > 0
                    "
                    class="mt-4"
                  >
                    <h3 class="text-subtitle-1 font-weight-medium mb-2">
                      Resources
                    </h3>
                    <v-list dense>
                      <v-list-item
                        v-for="(resource, index) in assignment.resources"
                        :key="index"
                        @click="downloadResource(resource)"
                      >
                        <v-list-item-icon>
                          <v-icon v-if="resource.type === 'PDF'"
                            >mdi-file-pdf-box</v-icon
                          >
                          <v-icon v-else-if="resource.type === 'DOC'"
                            >mdi-file-word</v-icon
                          >
                          <v-icon v-else-if="resource.type === 'IMG'"
                            >mdi-file-image</v-icon
                          >
                          <v-icon v-else>mdi-file</v-icon>
                        </v-list-item-icon>

                        <v-list-item-content>
                          <v-list-item-title>{{
                            resource.name
                          }}</v-list-item-title>
                          <v-list-item-subtitle>{{
                            formatFileSize(resource.size)
                          }}</v-list-item-subtitle>
                        </v-list-item-content>

                        <v-list-item-action>
                          <v-btn icon>
                            <v-icon>mdi-download</v-icon>
                          </v-btn>
                        </v-list-item-action>
                      </v-list-item>
                    </v-list>
                  </div>

                  <v-divider class="my-4"></v-divider>

                  <!-- Submitted Work Section -->
                  <div v-if="submission">
                    <h3 class="text-h6 mb-3">Your Submission</h3>

                    <div class="pa-4 rounded-lg grey lighten-4">
                      <div
                        class="d-flex justify-space-between align-center mb-2"
                      >
                        <div class="d-flex align-center">
                          <v-icon color="success" left>mdi-check-circle</v-icon>
                          <span class="text-subtitle-1 font-weight-medium"
                            >Submitted on
                            {{ formatDate(submission.submittedAt) }}</span
                          >
                        </div>

                        <v-chip
                          v-if="submission.status !== 'PENDING'"
                          :color="getStatusColor(submission.status)"
                          text-color="white"
                        >
                          {{ submission.status }}
                        </v-chip>
                      </div>

                      <div v-if="submission.content" class="mb-3">
                        <h4 class="text-subtitle-1 font-weight-medium mb-2">
                          Your Answer:
                        </h4>
                        <p>{{ submission.content }}</p>
                      </div>

                      <div v-if="submission.file">
                        <h4 class="text-subtitle-1 font-weight-medium mb-2">
                          Attached File:
                        </h4>
                        <v-btn text color="primary" @click="downloadSubmission">
                          <v-icon left>mdi-download</v-icon>
                          {{ submission.file.name }}
                          ({{ formatFileSize(submission.file.size) }})
                        </v-btn>
                      </div>

                      <!-- Feedback Section -->
                      <div
                        v-if="submission.status === 'GRADED'"
                        class="mt-4 pa-3 rounded-lg primary lighten-5"
                      >
                        <h4 class="text-subtitle-1 font-weight-medium mb-2">
                          <v-icon left color="primary"
                            >mdi-comment-text-outline</v-icon
                          >
                          Instructor Feedback
                        </h4>

                        <div
                          class="d-flex justify-space-between align-center mb-2"
                        >
                          <div>
                            <span class="font-weight-bold">Score:</span>
                          </div>
                          <v-chip
                            :color="
                              getScoreColor(
                                submission.score,
                                assignment.totalPoints
                              )
                            "
                            text-color="white"
                          >
                            {{ submission.score }} /
                            {{ assignment.totalPoints }}
                          </v-chip>
                        </div>

                        <p v-if="submission.feedback">
                          {{ submission.feedback }}
                        </p>
                        <p v-else class="text-caption grey--text">
                          No comments from instructor.
                        </p>
                      </div>
                    </div>
                  </div>

                  <!-- Submit Form -->
                  <div v-else>
                    <h3 class="text-h6 mb-3">Submit Your Work</h3>

                    <v-form
                      ref="form"
                      v-model="valid"
                      @submit.prevent="submitAssignment"
                    >
                      <v-textarea
                        v-model="submissionContent"
                        label="Your Answer"
                        rows="6"
                        counter
                        :rules="contentRules"
                      ></v-textarea>

                      <v-file-input
                        v-model="submissionFile"
                        label="Attach File (Optional)"
                        prepend-icon="mdi-paperclip"
                        :rules="fileRules"
                        show-size
                        counter
                        accept=".pdf,.doc,.docx,.zip,.rar,.jpg,.jpeg,.png"
                      ></v-file-input>

                      <v-checkbox
                        v-model="confirmSubmission"
                        label="I confirm this is my own work and complies with the academic integrity policy"
                        :rules="[
                          (v) => !!v || 'You must confirm before submitting',
                        ]"
                        required
                      ></v-checkbox>

                      <v-btn
                        color="primary"
                        :disabled="!valid || !confirmSubmission || loading"
                        :loading="loading"
                        type="submit"
                      >
                        Submit Assignment
                      </v-btn>
                    </v-form>
                  </div>
                </v-col>

                <v-col cols="12" md="4">
                  <v-card outlined>
                    <v-card-title>Assignment Details</v-card-title>

                    <v-card-text>
                      <v-list dense>
                        <v-list-item>
                          <v-list-item-icon>
                            <v-icon>mdi-school</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title>Course</v-list-item-title>
                            <v-list-item-subtitle>{{
                              assignment.courseName
                            }}</v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>

                        <v-list-item>
                          <v-list-item-icon>
                            <v-icon>mdi-calendar</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title>Due Date</v-list-item-title>
                            <v-list-item-subtitle>{{
                              formatDate(assignment.dueDate)
                            }}</v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>

                        <v-list-item>
                          <v-list-item-icon>
                            <v-icon>mdi-trophy</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title>Total Points</v-list-item-title>
                            <v-list-item-subtitle>{{
                              assignment.totalPoints
                            }}</v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>

                        <v-list-item>
                          <v-list-item-icon>
                            <v-icon>mdi-account-tie</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title>Instructor</v-list-item-title>
                            <v-list-item-subtitle>{{
                              assignment.instructorName
                            }}</v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>

                        <v-list-item v-if="submission">
                          <v-list-item-icon>
                            <v-icon>mdi-check-circle</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title>Status</v-list-item-title>
                            <v-list-item-subtitle>
                              {{
                                submission.status === "PENDING"
                                  ? "Awaiting grading"
                                  : "Graded"
                              }}
                            </v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>
                      </v-list>
                    </v-card-text>
                  </v-card>

                  <v-card
                    outlined
                    class="mt-4"
                    v-if="assignment.rubric && assignment.rubric.length > 0"
                  >
                    <v-card-title>Grading Rubric</v-card-title>

                    <v-card-text>
                      <v-simple-table dense>
                        <template v-slot:default>
                          <thead>
                            <tr>
                              <th>Criteria</th>
                              <th>Points</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr
                              v-for="(item, index) in assignment.rubric"
                              :key="index"
                            >
                              <td>{{ item.criteria }}</td>
                              <td>{{ item.points }}</td>
                            </tr>
                          </tbody>
                        </template>
                      </v-simple-table>
                    </v-card-text>
                  </v-card>
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import moment from "moment";

export default {
  props: {
    assignmentId: {
      type: String,
      required: true,
    },
    courseId: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      loading: false,
      assignment: {
        title: "",
        description: "",
        dueDate: null,
        totalPoints: 100,
        courseId: "",
        courseName: "",
        instructorName: "",
        resources: [],
        rubric: [],
      },
      submission: null,

      // Submit form data
      valid: false,
      submissionContent: "",
      submissionFile: null,
      confirmSubmission: false,

      // Validation rules
      contentRules: [
        (v) =>
          !!v || !!this.submissionFile || "Either text or file is required",
      ],
      fileRules: [
        (v) =>
          !v || v.size < 10000000 || "File size should be less than 10 MB!",
      ],
    };
  },

  created() {
    this.fetchAssignment();
    this.fetchSubmission();
  },

  methods: {
    async fetchAssignment() {
      try {
        this.loading = true;
        const response = await this.$axios.get(
          `/assignments/${this.assignmentId}`
        );
        this.assignment = response.data;
      } catch (error) {
        console.error("Failed to fetch assignment:", error);
        this.$notify("Failed to load assignment details", { color: "error" });
      } finally {
        this.loading = false;
      }
    },

    async fetchSubmission() {
      try {
        const response = await this.$axios.get(
          `/assignments/${this.assignmentId}/submission`
        );
        if (response.data) {
          this.submission = response.data;
        }
      } catch (error) {
        // No submission yet, this is expected for new assignments
        console.log("No existing submission found");
      }
    },

    async submitAssignment() {
      if (!this.$refs.form.validate()) return;

      if (!this.submissionContent && !this.submissionFile) {
        this.$notify("Please provide either text or file for your submission", {
          color: "warning",
        });
        return;
      }

      try {
        this.loading = true;

        const formData = new FormData();
        formData.append("content", this.submissionContent || "");

        if (this.submissionFile) {
          formData.append("file", this.submissionFile);
        }

        const response = await this.$axios.post(
          `/assignments/${this.assignmentId}/submit`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        this.submission = response.data;
        this.$notify("Assignment submitted successfully", { color: "success" });
      } catch (error) {
        console.error("Failed to submit assignment:", error);
        this.$notify(
          error.response?.data?.message || "Failed to submit assignment",
          { color: "error" }
        );
      } finally {
        this.loading = false;
      }
    },

    downloadResource(resource) {
      window.open(resource.url, "_blank");
    },

    downloadSubmission() {
      if (this.submission && this.submission.file) {
        window.open(this.submission.file.url, "_blank");
      }
    },

    formatDate(date) {
      return moment(date).format("MMM DD, YYYY [at] h:mm A");
    },

    formatFileSize(bytes) {
      if (bytes < 1024) return bytes + " bytes";
      if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB";
      return (bytes / 1048576).toFixed(1) + " MB";
    },

    getDueStatus() {
      if (!this.assignment.dueDate) return "No deadline";

      const now = moment();
      const dueDate = moment(this.assignment.dueDate);

      if (now.isAfter(dueDate)) {
        return "Overdue";
      }

      const daysLeft = dueDate.diff(now, "days");

      if (daysLeft === 0) {
        return "Due today";
      } else if (daysLeft === 1) {
        return "Due tomorrow";
      } else {
        return `${daysLeft} days left`;
      }
    },

    getDueStatusColor() {
      if (!this.assignment.dueDate) return "grey";

      const now = moment();
      const dueDate = moment(this.assignment.dueDate);

      if (now.isAfter(dueDate)) {
        return "error";
      }

      const daysLeft = dueDate.diff(now, "days");

      if (daysLeft <= 1) {
        return "warning";
      } else if (daysLeft <= 3) {
        return "orange";
      } else {
        return "success";
      }
    },

    getStatusColor(status) {
      switch (status) {
        case "GRADED":
          return "success";
        case "PENDING":
          return "info";
        case "LATE":
          return "warning";
        default:
          return "grey";
      }
    },

    getScoreColor(score, total) {
      const percentage = (score / total) * 100;

      if (percentage >= 90) return "success";
      if (percentage >= 75) return "primary";
      if (percentage >= 60) return "warning";
      return "error";
    },
  },
};
</script>

<style scoped>
/* Custom styles for the assignment submission page */
</style>
