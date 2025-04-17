<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-card>
            <v-card-title class="headline">
              <v-btn icon class="mr-2" :to="`/teacher/assignments`">
                <v-icon>mdi-arrow-left</v-icon>
              </v-btn>
              Grade Assignment: {{ assignment.title }}
            </v-card-title>

            <v-tabs v-model="activeTab">
              <v-tab>Student Submission</v-tab>
              <v-tab>Assignment Details</v-tab>
              <v-tab>Class Progress</v-tab>
            </v-tabs>

            <v-tabs-items v-model="activeTab">
              <!-- Student Submission Tab -->
              <v-tab-item>
                <v-card flat>
                  <v-card-text v-if="submission">
                    <v-row>
                      <v-col cols="12" md="8">
                        <div class="d-flex align-center mb-4">
                          <v-avatar size="40" class="mr-3">
                            <v-img
                              :src="
                                submission.student.avatar ||
                                'https://via.placeholder.com/40'
                              "
                            ></v-img>
                          </v-avatar>
                          <div>
                            <div class="text-h6">
                              {{ submission.student.name }}
                            </div>
                            <div class="text-caption">
                              Submitted {{ formatDate(submission.submittedAt) }}
                              <v-chip
                                x-small
                                class="ml-2"
                                :color="submission.onTime ? 'success' : 'error'"
                                text-color="white"
                              >
                                {{ submission.onTime ? "On Time" : "Late" }}
                              </v-chip>
                            </div>
                          </div>
                        </div>

                        <v-divider class="mb-4"></v-divider>

                        <h3 class="text-h6 mb-3">Student's Work</h3>

                        <div
                          v-if="submission.content"
                          class="submission-content"
                        >
                          <h4 class="text-subtitle-1 font-weight-medium mb-2">
                            Answer:
                          </h4>
                          <div class="pa-4 rounded grey lighten-4">
                            <p>{{ submission.content }}</p>
                          </div>
                        </div>

                        <div v-if="submission.file" class="mt-4">
                          <h4 class="text-subtitle-1 font-weight-medium mb-2">
                            Attached File:
                          </h4>
                          <v-card outlined>
                            <v-card-text>
                              <div class="d-flex align-center">
                                <v-icon
                                  v-if="isImage(submission.file)"
                                  size="40"
                                  class="mr-3"
                                  >mdi-file-image</v-icon
                                >
                                <v-icon
                                  v-else-if="isPdf(submission.file)"
                                  size="40"
                                  class="mr-3"
                                  >mdi-file-pdf-box</v-icon
                                >
                                <v-icon
                                  v-else-if="isDocument(submission.file)"
                                  size="40"
                                  class="mr-3"
                                  >mdi-file-document</v-icon
                                >
                                <v-icon v-else size="40" class="mr-3"
                                  >mdi-file</v-icon
                                >

                                <div class="flex-grow-1">
                                  <div>{{ submission.file.name }}</div>
                                  <div class="text-caption">
                                    {{ formatFileSize(submission.file.size) }}
                                  </div>
                                </div>

                                <v-btn icon @click="downloadSubmissionFile">
                                  <v-icon>mdi-download</v-icon>
                                </v-btn>

                                <v-btn
                                  icon
                                  @click="previewFile"
                                  v-if="canPreview(submission.file)"
                                >
                                  <v-icon>mdi-eye</v-icon>
                                </v-btn>
                              </div>
                            </v-card-text>
                          </v-card>
                        </div>

                        <v-divider class="my-4"></v-divider>

                        <h3 class="text-h6 mb-3">Grading</h3>

                        <v-form ref="gradeForm" v-model="gradeFormValid">
                          <v-row>
                            <v-col cols="12" sm="6">
                              <v-text-field
                                v-model.number="grade.score"
                                :rules="scoreRules"
                                label="Score"
                                type="number"
                                :min="0"
                                :max="assignment.totalPoints"
                                required
                                :suffix="`/ ${assignment.totalPoints}`"
                              ></v-text-field>
                            </v-col>

                            <v-col cols="12" sm="6">
                              <v-select
                                v-model="grade.status"
                                :items="gradeStatusOptions"
                                label="Status"
                                required
                              ></v-select>
                            </v-col>
                          </v-row>

                          <v-textarea
                            v-model="grade.feedback"
                            label="Feedback to Student"
                            rows="5"
                            hint="Provide constructive feedback to help the student improve"
                            persistent-hint
                          ></v-textarea>

                          <!-- Rubric Grading if available -->
                          <div
                            v-if="
                              assignment.rubric && assignment.rubric.length > 0
                            "
                            class="mt-4"
                          >
                            <h4 class="text-subtitle-1 font-weight-medium mb-3">
                              Rubric Assessment
                            </h4>

                            <v-simple-table>
                              <template v-slot:default>
                                <thead>
                                  <tr>
                                    <th>Criteria</th>
                                    <th>Available Points</th>
                                    <th>Score</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <tr
                                    v-for="(item, index) in assignment.rubric"
                                    :key="index"
                                  >
                                    <td>{{ item.criteria }}</td>
                                    <td>{{ item.points }}</td>
                                    <td>
                                      <v-text-field
                                        v-model.number="rubricScores[index]"
                                        type="number"
                                        :min="0"
                                        :max="item.points"
                                        dense
                                        hide-details
                                        @input="updateTotalScore"
                                      ></v-text-field>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td
                                      colspan="2"
                                      class="text-right font-weight-bold"
                                    >
                                      Total
                                    </td>
                                    <td>
                                      {{ calculateRubricTotal() }}/{{
                                        assignment.totalPoints
                                      }}
                                    </td>
                                  </tr>
                                </tbody>
                              </template>
                            </v-simple-table>
                          </div>

                          <div class="d-flex mt-4">
                            <v-spacer></v-spacer>
                            <v-btn
                              color="error"
                              text
                              class="mr-2"
                              @click="resetGrading"
                            >
                              Reset
                            </v-btn>
                            <v-btn
                              color="primary"
                              :disabled="!gradeFormValid || grading"
                              :loading="grading"
                              @click="submitGrade"
                            >
                              Submit Grade
                            </v-btn>
                          </div>
                        </v-form>
                      </v-col>

                      <v-col cols="12" md="4">
                        <v-card outlined>
                          <v-card-title>Student Information</v-card-title>

                          <v-card-text>
                            <v-list dense>
                              <v-list-item>
                                <v-list-item-icon>
                                  <v-icon>mdi-account</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                  <v-list-item-title>{{
                                    submission.student.name
                                  }}</v-list-item-title>
                                  <v-list-item-subtitle>{{
                                    submission.student.email
                                  }}</v-list-item-subtitle>
                                </v-list-item-content>
                              </v-list-item>

                              <v-list-item>
                                <v-list-item-icon>
                                  <v-icon>mdi-school</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                  <v-list-item-title
                                    >Course Progress</v-list-item-title
                                  >
                                  <v-list-item-subtitle>
                                    <v-progress-linear
                                      :value="submission.student.courseProgress"
                                      height="8"
                                      class="mt-1"
                                    >
                                      <template v-slot:default="{ value }">
                                        <span class="white--text text-caption"
                                          >{{ Math.ceil(value) }}%</span
                                        >
                                      </template>
                                    </v-progress-linear>
                                  </v-list-item-subtitle>
                                </v-list-item-content>
                              </v-list-item>

                              <v-list-item>
                                <v-list-item-icon>
                                  <v-icon>mdi-clock-outline</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                  <v-list-item-title
                                    >Submission Time</v-list-item-title
                                  >
                                  <v-list-item-subtitle>{{
                                    formatDate(submission.submittedAt)
                                  }}</v-list-item-subtitle>
                                </v-list-item-content>
                              </v-list-item>

                              <v-list-item v-if="!submission.onTime">
                                <v-list-item-icon>
                                  <v-icon color="error"
                                    >mdi-alert-circle</v-icon
                                  >
                                </v-list-item-icon>
                                <v-list-item-content>
                                  <v-list-item-title class="error--text"
                                    >Late Submission</v-list-item-title
                                  >
                                  <v-list-item-subtitle>
                                    {{
                                      getDaysLate(
                                        submission.submittedAt,
                                        assignment.dueDate
                                      )
                                    }}
                                    days after deadline
                                  </v-list-item-subtitle>
                                </v-list-item-content>
                              </v-list-item>
                            </v-list>
                          </v-card-text>
                        </v-card>

                        <v-card outlined class="mt-4">
                          <v-card-title>Previous Assignments</v-card-title>

                          <v-card-text>
                            <div v-if="studentPreviousAssignments.length > 0">
                              <v-list dense>
                                <v-list-item
                                  v-for="(
                                    prevAssignment, index
                                  ) in studentPreviousAssignments"
                                  :key="index"
                                >
                                  <v-list-item-content>
                                    <v-list-item-title>{{
                                      prevAssignment.title
                                    }}</v-list-item-title>
                                    <v-list-item-subtitle>
                                      <v-chip
                                        x-small
                                        :color="
                                          getScoreColor(
                                            prevAssignment.score,
                                            prevAssignment.totalPoints
                                          )
                                        "
                                        text-color="white"
                                      >
                                        {{ prevAssignment.score }} /
                                        {{ prevAssignment.totalPoints }}
                                      </v-chip>
                                    </v-list-item-subtitle>
                                  </v-list-item-content>
                                </v-list-item>
                              </v-list>
                            </div>
                            <div v-else class="text-center py-2">
                              <p class="text-caption grey--text">
                                No previous assignments
                              </p>
                            </div>
                          </v-card-text>
                        </v-card>
                      </v-col>
                    </v-row>
                  </v-card-text>

                  <v-card-text v-else>
                    <div class="text-center py-8">
                      <v-icon size="64" color="grey lighten-1"
                        >mdi-file-search-outline</v-icon
                      >
                      <h3 class="text-h5 mt-4 grey--text text--darken-1">
                        Submission not found
                      </h3>
                      <p class="text-subtitle-1 mt-2">
                        The student submission could not be found or has been
                        deleted.
                      </p>
                      <v-btn
                        color="primary"
                        class="mt-4"
                        :to="`/teacher/assignments`"
                      >
                        Back to Assignments
                      </v-btn>
                    </div>
                  </v-card-text>
                </v-card>
              </v-tab-item>

              <!-- Assignment Details Tab -->
              <v-tab-item>
                <v-card flat>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12" md="8">
                        <h3 class="text-h6 mb-3">Assignment Description</h3>
                        <div v-html="assignment.description"></div>

                        <v-divider class="my-4"></v-divider>

                        <div
                          v-if="
                            assignment.resources &&
                            assignment.resources.length > 0
                          "
                        >
                          <h3 class="text-h6 mb-3">Resources</h3>
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

                        <div
                          v-if="
                            assignment.rubric && assignment.rubric.length > 0
                          "
                        >
                          <h3 class="text-h6 mb-3">Rubric</h3>
                          <v-simple-table>
                            <template v-slot:default>
                              <thead>
                                <tr>
                                  <th>Criteria</th>
                                  <th>Description</th>
                                  <th>Points</th>
                                </tr>
                              </thead>
                              <tbody>
                                <tr
                                  v-for="(item, index) in assignment.rubric"
                                  :key="index"
                                >
                                  <td>{{ item.criteria }}</td>
                                  <td>{{ item.description }}</td>
                                  <td>{{ item.points }}</td>
                                </tr>
                                <tr>
                                  <td
                                    colspan="2"
                                    class="text-right font-weight-bold"
                                  >
                                    Total
                                  </td>
                                  <td>{{ assignment.totalPoints }}</td>
                                </tr>
                              </tbody>
                            </template>
                          </v-simple-table>
                        </div>
                      </v-col>

                      <v-col cols="12" md="4">
                        <v-card outlined>
                          <v-card-title>Assignment Details</v-card-title>

                          <v-card-text>
                            <v-list dense>
                              <v-list-item>
                                <v-list-item-icon>
                                  <v-icon>mdi-book-open-variant</v-icon>
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
                                  <v-list-item-title
                                    >Created Date</v-list-item-title
                                  >
                                  <v-list-item-subtitle>{{
                                    formatDate(assignment.createdAt)
                                  }}</v-list-item-subtitle>
                                </v-list-item-content>
                              </v-list-item>

                              <v-list-item>
                                <v-list-item-icon>
                                  <v-icon>mdi-clock-outline</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                  <v-list-item-title
                                    >Due Date</v-list-item-title
                                  >
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
                                  <v-list-item-title
                                    >Total Points</v-list-item-title
                                  >
                                  <v-list-item-subtitle>{{
                                    assignment.totalPoints
                                  }}</v-list-item-subtitle>
                                </v-list-item-content>
                              </v-list-item>
                            </v-list>
                          </v-card-text>
                        </v-card>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </v-card>
              </v-tab-item>

              <!-- Class Progress Tab -->
              <v-tab-item>
                <v-card flat>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <div
                          class="d-flex justify-space-between align-center mb-4"
                        >
                          <div>
                            <h3 class="text-h6">Class Submission Progress</h3>
                            <p class="text-subtitle-2 text-grey">
                              {{ stats.submittedCount }} out of
                              {{ stats.totalStudents }} students have submitted
                            </p>
                          </div>

                          <div>
                            <v-btn
                              color="primary"
                              :disabled="!hasUngradedSubmissions"
                              @click="gradeAllRemaining"
                            >
                              Grade All Remaining
                            </v-btn>
                          </div>
                        </div>

                        <v-progress-linear
                          :value="stats.submissionRate * 100"
                          height="20"
                          color="primary"
                          class="mb-4"
                        >
                          <template v-slot:default="{ value }">
                            <span class="white--text"
                              >{{ Math.ceil(value) }}% submitted</span
                            >
                          </template>
                        </v-progress-linear>

                        <v-row class="mb-6">
                          <v-col cols="12" sm="3">
                            <v-card outlined class="text-center pa-3">
                              <div class="text-h5 font-weight-bold">
                                {{ stats.submittedCount }}
                              </div>
                              <div class="text-caption">Submitted</div>
                            </v-card>
                          </v-col>

                          <v-col cols="12" sm="3">
                            <v-card outlined class="text-center pa-3">
                              <div class="text-h5 font-weight-bold">
                                {{ stats.gradedCount }}
                              </div>
                              <div class="text-caption">Graded</div>
                            </v-card>
                          </v-col>

                          <v-col cols="12" sm="3">
                            <v-card outlined class="text-center pa-3">
                              <div class="text-h5 font-weight-bold">
                                {{ stats.pendingCount }}
                              </div>
                              <div class="text-caption">Pending Grading</div>
                            </v-card>
                          </v-col>

                          <v-col cols="12" sm="3">
                            <v-card outlined class="text-center pa-3">
                              <div class="text-h5 font-weight-bold">
                                {{ stats.lateCount }}
                              </div>
                              <div class="text-caption">Late Submissions</div>
                            </v-card>
                          </v-col>
                        </v-row>

                        <v-divider class="mb-4"></v-divider>

                        <h3 class="text-h6 mb-3">Grade Distribution</h3>

                        <div class="mb-4">
                          <canvas ref="gradeChart" height="100"></canvas>
                        </div>

                        <v-divider class="mb-4"></v-divider>

                        <h3 class="text-h6 mb-3">All Student Submissions</h3>

                        <v-data-table
                          :headers="submissionHeaders"
                          :items="allSubmissions"
                          :search="search"
                          :items-per-page="10"
                          :loading="loading"
                          class="elevation-1"
                        >
                          <template v-slot:top>
                            <v-text-field
                              v-model="search"
                              prepend-icon="mdi-magnify"
                              label="Search Students"
                              class="mx-4 mt-4"
                              hide-details
                              clearable
                            ></v-text-field>
                          </template>

                          <template v-slot:item.student="{ item }">
                            <div class="d-flex align-center">
                              <v-avatar size="32" class="mr-2">
                                <v-img
                                  :src="
                                    item.student.avatar ||
                                    'https://via.placeholder.com/32'
                                  "
                                ></v-img>
                              </v-avatar>
                              <div>
                                {{ item.student.name }}
                              </div>
                            </div>
                          </template>

                          <template v-slot:item.status="{ item }">
                            <v-chip
                              small
                              :color="getSubmissionStatusColor(item.status)"
                              text-color="white"
                            >
                              {{ item.status }}
                            </v-chip>
                          </template>

                          <template v-slot:item.submittedAt="{ item }">
                            {{ formatDate(item.submittedAt) }}
                            <v-chip
                              v-if="!item.onTime"
                              x-small
                              color="error"
                              text-color="white"
                              class="ml-1"
                            >
                              LATE
                            </v-chip>
                          </template>

                          <template v-slot:item.score="{ item }">
                            <span v-if="item.status === 'GRADED'">
                              {{ item.score }} / {{ assignment.totalPoints }}
                            </span>
                            <span v-else>-</span>
                          </template>

                          <template v-slot:item.actions="{ item }">
                            <v-btn
                              icon
                              small
                              :to="`/teacher/assignments/${assignment.assignmentId}/submissions/${item.submissionId}`"
                              title="Grade Submission"
                              :disabled="
                                item.submissionId === submission?.submissionId
                              "
                            >
                              <v-icon>mdi-pencil</v-icon>
                            </v-btn>

                            <v-btn
                              icon
                              small
                              @click="downloadSubmissionFile(item)"
                              title="Download Submission"
                              :disabled="!item.file"
                            >
                              <v-icon>mdi-download</v-icon>
                            </v-btn>

                            <v-btn
                              icon
                              small
                              :href="`mailto:${item.student.email}`"
                              title="Email Student"
                            >
                              <v-icon>mdi-email</v-icon>
                            </v-btn>
                          </template>
                        </v-data-table>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </v-card>
              </v-tab-item>
            </v-tabs-items>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- File Preview Dialog -->
    <v-dialog v-model="filePreviewDialog" max-width="800">
      <v-card>
        <v-card-title class="headline">
          File Preview
          <v-spacer></v-spacer>
          <v-btn icon @click="filePreviewDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-card-text>
          <div v-if="isImage(submission.file)" class="text-center">
            <v-img :src="submission.file.url" max-height="500" contain></v-img>
          </div>
          <div v-else-if="isPdf(submission.file)" class="pdf-container">
            <iframe
              :src="submission.file.url"
              width="100%"
              height="500"
            ></iframe>
          </div>
          <div v-else class="text-center py-4">
            <v-icon size="64" color="grey lighten-1">mdi-file</v-icon>
            <p class="mt-2">
              This file type cannot be previewed. Please download it to view.
            </p>
            <v-btn color="primary" @click="downloadSubmissionFile"
              >Download</v-btn
            >
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import moment from "moment";
import Chart from "chart.js";

export default {
  props: {
    assignmentId: {
      type: String,
      required: true,
    },
    submissionId: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      activeTab: 0,
      loading: false,
      grading: false,
      search: "",
      gradeFormValid: false,
      filePreviewDialog: false,

      assignment: {
        assignmentId: "",
        title: "",
        description: "",
        courseId: "",
        courseName: "",
        createdAt: null,
        dueDate: null,
        totalPoints: 100,
        resources: [],
        rubric: [],
      },

      submission: null,
      allSubmissions: [],
      studentPreviousAssignments: [],

      grade: {
        score: null,
        status: "GRADED",
        feedback: "",
      },

      rubricScores: [],

      stats: {
        totalStudents: 0,
        submittedCount: 0,
        gradedCount: 0,
        pendingCount: 0,
        lateCount: 0,
        submissionRate: 0,
        gradeDistribution: {
          "A (90-100%)": 0,
          "B (80-89%)": 0,
          "C (70-79%)": 0,
          "D (60-69%)": 0,
          "F (0-59%)": 0,
        },
      },

      gradeStatusOptions: [
        { text: "Graded", value: "GRADED" },
        { text: "Incomplete", value: "INCOMPLETE" },
        { text: "Resubmit Requested", value: "RESUBMIT" },
      ],

      submissionHeaders: [
        { text: "Student", value: "student", sortable: true },
        { text: "Status", value: "status", sortable: true },
        { text: "Submitted", value: "submittedAt", sortable: true },
        { text: "Score", value: "score", sortable: true },
        { text: "Actions", value: "actions", sortable: false, align: "center" },
      ],

      scoreRules: [
        (v) => v !== null || "Score is required",
        (v) => v >= 0 || "Score cannot be negative",
        (v) =>
          v <= this.assignment.totalPoints ||
          `Score cannot exceed ${this.assignment.totalPoints} points`,
      ],
    };
  },

  computed: {
    hasUngradedSubmissions() {
      return this.stats.pendingCount > 0;
    },
  },

  created() {
    this.fetchAssignment();
    this.fetchSubmission();
    this.fetchAllSubmissions();
  },

  mounted() {
    this.initGradeChart();
  },

  methods: {
    async fetchAssignment() {
      try {
        this.loading = true;
        const response = await this.$axios.get(
          `/assignments/${this.assignmentId}`
        );
        this.assignment = response.data;

        // Initialize rubric scores array if rubric exists
        if (this.assignment.rubric && this.assignment.rubric.length > 0) {
          this.rubricScores = this.assignment.rubric.map(() => 0);
        }
      } catch (error) {
        console.error("Failed to fetch assignment:", error);
        this.$notify("Failed to load assignment details", { color: "error" });
      } finally {
        this.loading = false;
      }
    },

    async fetchSubmission() {
      try {
        this.loading = true;
        const response = await this.$axios.get(
          `/assignments/submissions/${this.submissionId}`
        );
        this.submission = response.data;

        // Pre-fill grade data if submission is already graded
        if (this.submission.status === "GRADED") {
          this.grade.score = this.submission.score;
          this.grade.feedback = this.submission.feedback || "";

          // Pre-fill rubric scores if available
          if (this.submission.rubricScores && this.assignment.rubric) {
            this.rubricScores = this.submission.rubricScores;
          }
        } else {
          // Default score to total points for convenience
          this.grade.score = this.assignment.totalPoints;
        }

        // Fetch student's previous assignments
        this.fetchStudentPreviousAssignments();
      } catch (error) {
        console.error("Failed to fetch submission:", error);
        this.$notify("Failed to load student submission", { color: "error" });
      } finally {
        this.loading = false;
      }
    },

    async fetchAllSubmissions() {
      try {
        const response = await this.$axios.get(
          `/assignments/${this.assignmentId}/submissions`
        );
        this.allSubmissions = response.data.submissions;
        this.stats = response.data.stats;

        // Update grade chart
        this.updateGradeChart();
      } catch (error) {
        console.error("Failed to fetch all submissions:", error);
      }
    },

    async fetchStudentPreviousAssignments() {
      if (!this.submission || !this.submission.student) return;

      try {
        const studentId = this.submission.student.id;
        const response = await this.$axios.get(
          `/students/${studentId}/assignments?courseId=${this.assignment.courseId}`
        );

        // Filter out current assignment
        this.studentPreviousAssignments = response.data.filter(
          (a) => a.assignmentId !== this.assignmentId && a.status === "GRADED"
        );
      } catch (error) {
        console.error("Failed to fetch student previous assignments:", error);
      }
    },

    async submitGrade() {
      if (!this.$refs.gradeForm.validate()) return;

      this.grading = true;

      try {
        const gradeData = {
          score: this.grade.score,
          status: this.grade.status,
          feedback: this.grade.feedback,
          rubricScores: this.assignment.rubric ? this.rubricScores : undefined,
        };

        const response = await this.$axios.post(
          `/assignments/submissions/${this.submission.submissionId}/grade`,
          gradeData
        );

        this.submission = response.data;
        this.$notify("Assignment graded successfully", { color: "success" });

        // Refresh all submissions to update stats
        this.fetchAllSubmissions();
      } catch (error) {
        console.error("Failed to submit grade:", error);
        this.$notify(
          error.response?.data?.message || "Failed to submit grade",
          { color: "error" }
        );
      } finally {
        this.grading = false;
      }
    },

    resetGrading() {
      this.grade.score = this.assignment.totalPoints;
      this.grade.status = "GRADED";
      this.grade.feedback = "";

      if (this.assignment.rubric) {
        this.rubricScores = this.assignment.rubric.map(() => 0);
      }
    },

    calculateRubricTotal() {
      return this.rubricScores.reduce((sum, score) => sum + score, 0);
    },

    updateTotalScore() {
      this.grade.score = this.calculateRubricTotal();
    },

    downloadSubmissionFile(customSubmission = null) {
      const sub = customSubmission || this.submission;
      if (sub && sub.file) {
        window.open(sub.file.url, "_blank");
      }
    },

    downloadResource(resource) {
      window.open(resource.url, "_blank");
    },

    previewFile() {
      if (
        this.submission &&
        this.submission.file &&
        this.canPreview(this.submission.file)
      ) {
        this.filePreviewDialog = true;
      }
    },

    canPreview(file) {
      return this.isImage(file) || this.isPdf(file);
    },

    isImage(file) {
      return file && /\.(jpe?g|png|gif|webp)$/i.test(file.name);
    },

    isPdf(file) {
      return file && /\.pdf$/i.test(file.name);
    },

    isDocument(file) {
      return file && /\.(doc|docx|ppt|pptx|xls|xlsx)$/i.test(file.name);
    },

    formatDate(date) {
      return moment(date).format("MMM DD, YYYY [at] h:mm A");
    },

    formatFileSize(bytes) {
      if (bytes < 1024) return bytes + " bytes";
      if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB";
      return (bytes / 1048576).toFixed(1) + " MB";
    },

    getDaysLate(submittedDate, dueDate) {
      const submitted = moment(submittedDate);
      const due = moment(dueDate);
      return submitted.diff(due, "days");
    },

    getSubmissionStatusColor(status) {
      switch (status) {
        case "GRADED":
          return "success";
        case "PENDING":
          return "info";
        case "INCOMPLETE":
          return "error";
        case "RESUBMIT":
          return "warning";
        default:
          return "grey";
      }
    },

    getScoreColor(score, total) {
      const percentage = (score / total) * 100;

      if (percentage >= 90) return "success";
      if (percentage >= 80) return "primary";
      if (percentage >= 70) return "info";
      if (percentage >= 60) return "warning";
      return "error";
    },

    initGradeChart() {
      const ctx = document.createElement("canvas");
      this.$refs.gradeChart.appendChild(ctx);

      this.gradeChart = new Chart(ctx, {
        type: "bar",
        data: {
          labels: Object.keys(this.stats.gradeDistribution),
          datasets: [
            {
              label: "Number of Students",
              data: Object.values(this.stats.gradeDistribution),
              backgroundColor: [
                "rgba(76, 175, 80, 0.6)", // A - Green
                "rgba(33, 150, 243, 0.6)", // B - Blue
                "rgba(255, 193, 7, 0.6)", // C - Yellow
                "rgba(255, 152, 0, 0.6)", // D - Orange
                "rgba(244, 67, 54, 0.6)", // F - Red
              ],
              borderColor: [
                "rgba(76, 175, 80, 1)",
                "rgba(33, 150, 243, a1)",
                "rgba(255, 193, 7, 1)",
                "rgba(255, 152, 0, 1)",
                "rgba(244, 67, 54, 1)",
              ],
              borderWidth: 1,
            },
          ],
        },
        options: {
          responsive: true,
          scales: {
            yAxes: [
              {
                ticks: {
                  beginAtZero: true,
                  stepSize: 1,
                },
              },
            ],
          },
        },
      });
    },

    updateGradeChart() {
      if (!this.gradeChart) return;

      this.gradeChart.data.labels = Object.keys(this.stats.gradeDistribution);
      this.gradeChart.data.datasets[0].data = Object.values(
        this.stats.gradeDistribution
      );
      this.gradeChart.update();
    },

    async gradeAllRemaining() {
      // Show confirmation dialog
      const confirm = window.confirm(
        "Are you sure you want to grade all remaining submissions? " +
          "This will use the current score and feedback for all pending submissions."
      );

      if (!confirm) return;

      this.grading = true;

      try {
        const gradeData = {
          score: this.grade.score,
          status: this.grade.status,
          feedback: this.grade.feedback || "Graded in batch",
        };

        const response = await this.$axios.post(
          `/assignments/${this.assignmentId}/grade-all-pending`,
          gradeData
        );

        this.$notify(`Successfully graded ${response.data.count} submissions`, {
          color: "success",
        });

        // Refresh all submissions to update stats
        this.fetchAllSubmissions();
      } catch (error) {
        console.error("Failed to grade all submissions:", error);
        this.$notify(
          error.response?.data?.message || "Failed to grade all submissions",
          { color: "error" }
        );
      } finally {
        this.grading = false;
      }
    },
  },
};
</script>

<style scoped>
.pdf-container {
  width: 100%;
  height: 500px;
  overflow: hidden;
}

.submission-content {
  white-space: pre-wrap;
}
</style>
