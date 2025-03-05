; ModuleID = 'llvm-link'
source_filename = "llvm-link"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [3 x i8] c"%c\00", align 1
@.str.1 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.2 = private unnamed_addr constant [4 x i8] c"%d:\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c" %d\00", align 1
@.str.4 = private unnamed_addr constant [2 x i8] c"\0A\00", align 1
@.str.5 = private unnamed_addr constant [3 x i8] c"%s\00", align 1

define dso_local i32 @modify_array(ptr %array_param_0) {
  %func_2 = alloca i32, align 4
  %func_3 = alloca i8, align 1
  %array_param_9 = alloca ptr, align 8
  store ptr %array_param_0, ptr %array_param_9, align 8
  %func_10 = load ptr, ptr %array_param_9, align 8
  store i32 1, ptr %func_2, align 4
  store i8 43, ptr %func_3, align 1
  %func_4 = load i8, ptr %func_3, align 1
  %func_5 = zext i8 %func_4 to i32
  %func_6 = zext i8 43 to i32
  %func_7 = icmp eq i32 %func_5, %func_6
  br i1 %func_7, label %func_8, label %func_19

func_8:                                           ; preds = %0
  %func_11 = load i32, ptr %func_2, align 4
  %func_12 = getelementptr inbounds i32, ptr %func_10, i32 %func_11
  %func_13 = load i32, ptr %func_2, align 4
  %func_14 = getelementptr inbounds i32, ptr %func_10, i32 %func_13
  %func_15 = load i32, ptr %func_14, align 4
  %func_16 = load i32, ptr %func_2, align 4
  %func_17 = add nsw i32 %func_15, %func_16
  %func_18 = load i32, ptr %func_2, align 4
  store i32 %func_17, ptr %func_12, align 4
  br label %func_28

func_19:                                          ; preds = %0
  %func_20 = load i32, ptr %func_2, align 4
  %func_21 = getelementptr inbounds i32, ptr %func_10, i32 %func_20
  %func_22 = load i32, ptr %func_2, align 4
  %func_23 = getelementptr inbounds i32, ptr %func_10, i32 %func_22
  %func_24 = load i32, ptr %func_23, align 4
  %func_25 = load i32, ptr %func_2, align 4
  %func_26 = sub nsw i32 %func_24, %func_25
  %func_27 = load i32, ptr %func_2, align 4
  store i32 %func_26, ptr %func_21, align 4
  br label %func_28

func_28:                                          ; preds = %func_19, %func_8
  ret i32 0
}

define dso_local i32 @func(i32 %param_0, i8 %param_1) {
  %param_3 = alloca i32, align 4
  %param_5 = alloca i8, align 1
  store i32 %param_0, ptr %param_3, align 4
  store i8 %param_1, ptr %param_5, align 1
  %func_4 = load i32, ptr %param_3, align 4
  %func_6 = load i8, ptr %param_5, align 1
  %func_7 = zext i8 %func_6 to i32
  %func_8 = add nsw i32 %func_4, %func_7
  ret i32 %func_8
}

define dso_local i32 @main() {
  %main_1 = alloca [5 x i32], align 4
  %main_7 = alloca i32, align 4
  %main_2 = getelementptr inbounds [5 x i32], ptr %main_1, i32 0, i32 0
  store i32 1, ptr %main_2, align 4
  %main_3 = getelementptr inbounds [5 x i32], ptr %main_1, i32 0, i32 1
  store i32 2, ptr %main_3, align 4
  %main_4 = getelementptr inbounds [5 x i32], ptr %main_1, i32 0, i32 2
  store i32 0, ptr %main_4, align 4
  %main_5 = getelementptr inbounds [5 x i32], ptr %main_1, i32 0, i32 3
  store i32 0, ptr %main_5, align 4
  %main_6 = getelementptr inbounds [5 x i32], ptr %main_1, i32 0, i32 4
  store i32 0, ptr %main_6, align 4
  %main_8 = getelementptr inbounds [5 x i32], ptr %main_1, i32 0, i32 0
  %main_9 = call i32 @modify_array(ptr %main_8)
  store i32 %main_9, ptr %main_7, align 4
  ret i32 0
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @getchar() #0 {
  %1 = alloca i8, align 1
  %2 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef @.str, ptr noundef %1)
  %3 = load i8, ptr %1, align 1
  %4 = sext i8 %3 to i32
  ret i32 %4
}

declare i32 @__isoc99_scanf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @getint() #0 {
  %1 = alloca i32, align 4
  %2 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef @.str.1, ptr noundef %1)
  br label %3

3:                                                ; preds = %6, %0
  %4 = call i32 @getchar()
  %5 = icmp ne i32 %4, 10
  br i1 %5, label %6, label %7

6:                                                ; preds = %3
  br label %3, !llvm.loop !6

7:                                                ; preds = %3
  %8 = load i32, ptr %1, align 4
  ret i32 %8
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @getarray(ptr noundef %0) #0 {
  %2 = alloca ptr, align 8
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %2, align 8
  %5 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef @.str.1, ptr noundef %3)
  store i32 0, ptr %4, align 4
  br label %6

6:                                                ; preds = %16, %1
  %7 = load i32, ptr %4, align 4
  %8 = load i32, ptr %3, align 4
  %9 = icmp slt i32 %7, %8
  br i1 %9, label %10, label %19

10:                                               ; preds = %6
  %11 = load ptr, ptr %2, align 8
  %12 = load i32, ptr %4, align 4
  %13 = sext i32 %12 to i64
  %14 = getelementptr inbounds i32, ptr %11, i64 %13
  %15 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef @.str.1, ptr noundef %14)
  br label %16

16:                                               ; preds = %10
  %17 = load i32, ptr %4, align 4
  %18 = add nsw i32 %17, 1
  store i32 %18, ptr %4, align 4
  br label %6, !llvm.loop !8

19:                                               ; preds = %6
  %20 = load i32, ptr %3, align 4
  ret i32 %20
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putint(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.1, i32 noundef %3)
  ret void
}

declare i32 @printf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putch(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str, i32 noundef %3)
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putarray(i32 noundef %0, ptr noundef %1) #0 {
  %3 = alloca i32, align 4
  %4 = alloca ptr, align 8
  %5 = alloca i32, align 4
  store i32 %0, ptr %3, align 4
  store ptr %1, ptr %4, align 8
  %6 = load i32, ptr %3, align 4
  %7 = call i32 (ptr, ...) @printf(ptr noundef @.str.2, i32 noundef %6)
  store i32 0, ptr %5, align 4
  br label %8

8:                                                ; preds = %19, %2
  %9 = load i32, ptr %5, align 4
  %10 = load i32, ptr %3, align 4
  %11 = icmp slt i32 %9, %10
  br i1 %11, label %12, label %22

12:                                               ; preds = %8
  %13 = load ptr, ptr %4, align 8
  %14 = load i32, ptr %5, align 4
  %15 = sext i32 %14 to i64
  %16 = getelementptr inbounds i32, ptr %13, i64 %15
  %17 = load i32, ptr %16, align 4
  %18 = call i32 (ptr, ...) @printf(ptr noundef @.str.3, i32 noundef %17)
  br label %19

19:                                               ; preds = %12
  %20 = load i32, ptr %5, align 4
  %21 = add nsw i32 %20, 1
  store i32 %21, ptr %5, align 4
  br label %8, !llvm.loop !9

22:                                               ; preds = %8
  %23 = call i32 (ptr, ...) @printf(ptr noundef @.str.4)
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putstr(ptr noundef %0) #0 {
  %2 = alloca ptr, align 8
  store ptr %0, ptr %2, align 8
  %3 = load ptr, ptr %2, align 8
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.5, ptr noundef %3)
  ret void
}

attributes #0 = { noinline nounwind optnone uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }

!llvm.ident = !{!0}
!llvm.module.flags = !{!1, !2, !3, !4, !5}

!0 = !{!"Ubuntu clang version 18.1.3 (1ubuntu1)"}
!1 = !{i32 1, !"wchar_size", i32 4}
!2 = !{i32 8, !"PIC Level", i32 2}
!3 = !{i32 7, !"PIE Level", i32 2}
!4 = !{i32 7, !"uwtable", i32 2}
!5 = !{i32 7, !"frame-pointer", i32 2}
!6 = distinct !{!6, !7}
!7 = !{!"llvm.loop.mustprogress"}
!8 = distinct !{!8, !7}
!9 = distinct !{!9, !7}
