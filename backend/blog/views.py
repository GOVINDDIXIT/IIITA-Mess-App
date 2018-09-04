import datetime

from django.utils import timezone
from django.shortcuts import render
from django.http import HttpResponse
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from review.models import ReviewQuestion


def homepage(request):
    status = True
    all_objects = ReviewQuestion.objects.all()
    if not all_objects.exists():
        status = False
    all_dates_set = {object_.timestamp.date() for object_ in all_objects}
    all_dates_list = list(all_dates_set)
    all_dates_list.sort(reverse=True)
    reports = []
    for date in all_dates_list:
        queryset = ReviewQuestion.objects.filter(timestamp__date=date, ques_type='DAILY')
        reports.append(queryset)
    page = request.GET.get('page', 1)
    paginator = Paginator(reports, 20)
    try:
        posts = paginator.page(page)
    except PageNotAnInteger:
        posts = paginator.page(1)
    except EmptyPage:
        posts = paginator.page(paginator.num_pages)

    return render(request, 'blog/index.html', {'posts': posts, 'status': status})


def view_post(request, date):
    report = ReviewQuestion.objects.filter(timestamp__date=date)
    status = report.exists()
    return render(request, 'blog/view_post.html', {'report': report, 'status': status, 'date': date})


def previous_reports(request):
    if request.method == 'POST':
        date = request.POST.get('date')
        queryset = ReviewQuestion.objects.filter(timestamp__date=date)
        return render(request, 'blog/view_post.html', {'report': queryset, 'status': queryset.exists(), 'date': date})
    return render(request, 'blog/previous_report.html', {'today': timezone.now().date})


def developers(request):
    return render(request, 'blog/developers.html', {})


@csrf_exempt
def today_report_submitted(request):
    queryset = ReviewQuestion.objects.filter(
        timestamp__date=timezone.now().date())
    if not queryset.exists():
        return JsonResponse({'detail': 'Report Not Submitted'}, status=200)
    else:
        return JsonResponse({'detail': 'Report Already Submitted'}, status=403)
